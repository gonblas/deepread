import React, { createContext, useContext, useEffect, useState } from "react";
import Cookies from "js-cookie";
import { useAuth } from "./authContext";

interface BarChartInteractiveProps {
  date: string;
  attempts: number;
  averageScore: number;
}

export type Stats = {
  totalAttempts: number;
  totalQuizzesAttempted: number;
  averageScore: number;
  bestScore: number;
  worstScore: number;
  averageTimeSeconds: number;
  dailyStatsTimeline: BarChartInteractiveProps[];
};

interface StatisticsContextValue {
  stats: Stats;
  loading: boolean;
  error: string | null;
  refresh: () => void;
}

const StatisticsContext = createContext<StatisticsContextValue | undefined>(undefined);

export function StatisticsProvider({ children }: { children: React.ReactNode }) {
  const { logout, user } = useAuth();
  const [stats, setStats] = useState<Stats>({
    totalAttempts: 0,
    totalQuizzesAttempted: 0,
    averageScore: 0,
    bestScore: 0,
    worstScore: 0,
    averageTimeSeconds: 0,
    dailyStatsTimeline: [],
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchStats = () => {
    setLoading(true);
    setError(null);

    if (user === null) {return;}

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
          throw new Error(text || "Failed to fetch stats");
        }
        return response.json();
      })
      .then((data) => {
        setStats(data.stats);
      })
      .catch((err) => {
        if (err.status === 401) {
          logout();
          return;
        }
        console.error("Error fetching user stats:", err);
        setError(err.message || "Unknown error");
      })
      .finally(() => {
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchStats();
  }, [user]);

  return (
    <StatisticsContext.Provider value={{ stats, loading, error, refresh: fetchStats }}>
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
