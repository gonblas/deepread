import React, { createContext, useContext, useState } from "react";
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

interface StatisticsContextValue {
  loading: boolean;
  error: string | null;
  stats: Stats;
  dailyStatsTimeline: BarChartInteractiveProps[];
  fetchUserStats: () => void;
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

  // Estado separado para las estadísticas generales
  const [stats, setStats] = useState<Stats>({
    totalAttempts: 0,
    totalQuizzesAttempted: 0,
    averageScore: 0,
    bestScore: 0,
    worstScore: 0,
    averageTimeSeconds: 0,
  });

  const [dailyStatsTimeline, setDailyStatsTimeline] = useState<
    BarChartInteractiveProps[]
  >([]);

  const [loading, setLoading] = useState(true);
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
        if (!response.ok) {
          const text = await response.text();
          throw {
            status: response.status,
            message: text || "Failed to fetch stats",
          };
        }
        if (response.status === 204) {
          return {
            stats: {
              totalAttempts: 0,
              totalQuizzesAttempted: 0,
              averageScore: 0,
              bestScore: 0,
              worstScore: 0,
              averageTimeSeconds: 0,
            },
            dailyStatsTimeline: [],};
        }
        return response.json();
      })
      .then((data) => {
        setStats({
          totalAttempts: data.stats.totalAttempts,
          totalQuizzesAttempted: data.stats.totalQuizzesAttempted,
          averageScore: data.stats.averageScore,
          bestScore: data.stats.bestScore,
          worstScore: data.stats.worstScore,
          averageTimeSeconds: data.stats.averageTimeSeconds,
        });
        setDailyStatsTimeline(data.stats.setDailyStatsTimeline || []);
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
      value={{ loading, error, stats, dailyStatsTimeline, fetchUserStats }}
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
