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

export type BookStatsResponse = StatsWithTimeline & {
  bookId: string;
  bookTitle?: string;
};

interface StatisticsContextValue {
  loading: boolean;
  error: string | null;

  userStats: StatsWithTimeline;
  fetchUserStats: () => void;

  quizStats: QuizStatsResponse | null;
  fetchQuizStats: (chapterId: string) => void;

  bookStats: BookStatsResponse | null;
  fetchBookStats: (bookId: string) => void;
}

const StatisticsContext = createContext<StatisticsContextValue | undefined>(
  undefined
);

export function StatisticsProvider({
  children,
}: {
  children: React.ReactNode;
}) {
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

  const [userStats, setUserStats] = useState<StatsWithTimeline>(
    emptyStatsWithTimeline
  );
  const [quizStats, setQuizStats] = useState<QuizStatsResponse | null>(null);
  const [bookStats, setBookStats] = useState<BookStatsResponse | null>(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

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
        if (response === null || !response.ok) {
          const text = await response.text();
          throw {
            status: response?.status,
            message: text || "Failed to fetch stats",
          };
        }
        if (response.status === 204) {
          return emptyStatsWithTimeline;
        }
        return response.json();
      })
      .then((data) => {
        const mappedStats: StatsWithTimeline = {
          stats: data.stats,
          dailyStatsTimeline: data.stats.dailyStatsTimeline || [],
        };
        setUserStats(mappedStats);
      })
      .catch((err) => {
        if (err.status === 401) {
          logout();
          return;
        }
        if (err instanceof TypeError) {
          setError(
            "Cannot connect to the server. Please check your internet connection or try again later."
          );
          return;
        }
        setError(err.message || "Unknown error");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const fetchQuizStats = (chapterId: string) => {
    setLoading(true);
    setError(null);

    if (isLoading) {
      setLoading(false);
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
        if (!text)
          return {
            chapterId,
            chapterTitle: "",
            ...emptyStatsWithTimeline,
          };
        return JSON.parse(text);
      })
      .then((data) => {
        const mappedStats: StatsWithTimeline = {
          stats: data.stats,
          dailyStatsTimeline: data.stats.dailyStatsTimeline || [],
        };
        setQuizStats({
          chapterId,
          chapterTitle: data.chapterTitle ?? "",
          ...mappedStats,
        });
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

  const fetchBookStats = (bookId: string) => {
    setLoading(true);
    setError(null);

    if (isLoading) {
      setLoading(false);
      return;
    }

    fetch(`http://localhost:8080/api/statistics/books/${bookId}`, {
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
            message: text || "Failed to fetch book stats",
          };
        }
        if (response.status === 204) {
          return {
            bookId,
            ...emptyStatsWithTimeline,
          };
        }
        const text = await response.text();
        if (!text) {
          return {
            bookId,
            ...emptyStatsWithTimeline,
          };
        }
        return JSON.parse(text);
      })
      .then((data) => {
        const mappedStats: StatsWithTimeline = {
          stats: data.stats,
          dailyStatsTimeline: data.stats.dailyStatsTimeline || [],
        };
        setBookStats({
          bookId,
          bookTitle: data.bookTitle ?? "",
          ...mappedStats,
        });
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

  return (
    <StatisticsContext.Provider
      value={{
        loading,
        error,
        userStats,
        fetchUserStats,
        quizStats,
        fetchQuizStats,
        bookStats,
        fetchBookStats,
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
