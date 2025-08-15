"use client";

import type React from "react";
import { createContext, useContext, useState } from "react";
import Cookies from "js-cookie";
import { useAuth } from "./authContext";

export type BarChartInteractiveProps = {
  date: string;
  attempts: number;
  averageScore: number;
};

export type Stats = {
  totalAttempts: number;
  totalQuizzesAttempted: number;
  averageScore: number;
  bestScore: number;
  worstScore: number;
  averageTimeSeconds: number;
};

export type StatsWithTimeline = {
  stats: Stats;
  dailyStatsTimeline: BarChartInteractiveProps[];
};

export type QuizStatsResponse = StatsWithTimeline & {
  chapterId?: string;
  chapterTitle?: string;
};

interface StatisticsContextValue {
  // User stats
  loading: boolean;
  error: string | null;
  userStats: StatsWithTimeline;
  fetchUserStats: () => void;

  // Quiz specific stats
  quizLoading: boolean;
  quizError: string | null;
  quizStats: QuizStatsResponse | null;
  fetchQuizStats: (chapterId: string) => void;
}

const StatisticsContext = createContext<StatisticsContextValue | undefined>(undefined);

export function StatisticsProvider({ children }: { children: React.ReactNode }) {
  const { logout, isLoading } = useAuth();

  const emptyStats: Stats = {
    totalAttempts: 0,
    totalQuizzesAttempted: 0,
    averageScore: 0,
    bestScore: 0,
    worstScore: 0,
    averageTimeSeconds: 0,
  };

  const emptyStatsWithTimeline: StatsWithTimeline = {
    stats: emptyStats,
    dailyStatsTimeline: [],
  };

  const [userStats, setUserStats] = useState<StatsWithTimeline>(emptyStatsWithTimeline);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [quizStats, setQuizStats] = useState<QuizStatsResponse | null>(null);
  const [quizLoading, setQuizLoading] = useState(false);
  const [quizError, setQuizError] = useState<string | null>(null);

  const fetchUserStats = () => {
    setLoading(true);
    setError(null);

    if (isLoading) {
      setLoading(false);
      return;
    }

    fetch("http://localhost:8080/api/statistics/user", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
    })
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw {
            status: response.status,
            message: text || "Failed to fetch stats",
          };
        }
        if (response.status === 204) {
          return emptyStatsWithTimeline;
        }
        const text = await response.text();
        if (!text) return emptyStatsWithTimeline;
        return JSON.parse(text);
      })
      .then((data: StatsWithTimeline) => {
        setUserStats(data);
      })
      .catch((err) => {
        if (err.status === 401) {
          logout();
          return;
        }
        setError(err.message || "Unknown error");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchQuizStats = (chapterId: string) => {
    setQuizLoading(true);
    setQuizError(null);

    if (isLoading) {
      setQuizLoading(false);
      return;
    }

    fetch(`http://localhost:8080/api/statistics/chapters/${chapterId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
    })
      .then(async (response) => {
        if (!response.ok) {
          const text = await response.text();
          throw {
            status: response.status,
            message: text || "Failed to fetch quiz stats",
          };
        }
        if (response.status === 204) {
          return {
            chapterId,
            chapterTitle: "",
            ...emptyStatsWithTimeline,
          };
        }
        const text = await response.text();
        if (!text) return {
          chapterId,
          chapterTitle: "",
          ...emptyStatsWithTimeline,
        };
        return JSON.parse(text);
      })
      .then((data: QuizStatsResponse) => {
        setQuizStats(data);
      })
      .catch((err) => {
        if (err.status === 401) {
          logout();
          return;
        }
        setQuizError(err.message || "Unknown error");
      })
      .finally(() => {
        setQuizLoading(false);
      });
    setQuizLoading(false);
  };

  return (
    <StatisticsContext.Provider
      value={{
        // User stats
        loading,
        error,
        userStats,
        fetchUserStats,
        // Quiz stats
        quizLoading,
        quizError,
        quizStats,
        fetchQuizStats,
      }}
    >
      {children}
    </StatisticsContext.Provider>
  );
}

export function useStatistics() {
  const context = useContext(StatisticsContext);
  if (!context) {
    throw new Error("useStatistics must be used within a StatisticsProvider");
  }
  return context;
}
