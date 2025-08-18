"use client";

import { createContext, useContext, useState, ReactNode, useCallback } from "react";
import Cookies from "js-cookie";

export interface RecentAttempt {
  id: string;
  bookTitle: string;
  chapterNumber: number;
  score: number;
  submittedAt: string;
}

interface RecentAttemptsContextType {
  userAttempts: RecentAttempt[];
  bookAttempts: RecentAttempt[];
  quizAttempts: RecentAttempt[];
  loading: boolean;
  error: string | null;
  fetchUserRecentAttempts: (page?: number, size?: number) => Promise<void>;
  fetchBookRecentAttempts: (bookId: string, page?: number, size?: number) => Promise<void>;
  fetchQuizRecentAttempts: (quizId: string, page?: number, size?: number) => Promise<void>;
}

const RecentAttemptsContext = createContext<RecentAttemptsContextType | undefined>(undefined);

export const RecentAttemptsProvider = ({ children }: { children: ReactNode }) => {
  const [userAttempts, setUserAttempts] = useState<RecentAttempt[]>([]);
  const [bookAttempts, setBookAttempts] = useState<RecentAttempt[]>([]);
  const [quizAttempts, setQuizAttempts] = useState<RecentAttempt[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const token = Cookies.get("token");

  const fetchUserRecentAttempts = useCallback(async (page = 0, size = 5) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost:8080/api/attempts/recent?page=${page}&size=${size}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to fetch user recent attempts");
      const data = await res.json();
      setUserAttempts(data.content ?? data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token]);


  const fetchBookRecentAttempts = useCallback(async (bookId: string, page = 0, size = 5) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost:8080/api/books/${bookId}/attempts/recent?page=${page}&size=${size}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to fetch book recent attempts");
      const data = await res.json();
      setBookAttempts(data.content ?? data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token]);


  const fetchQuizRecentAttempts = useCallback(async (quizId: string, page = 0, size = 5) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fetch(`http://localhost:8080/api/quizzes/${quizId}/attempts/recent?page=${page}&size=${size}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!res.ok) throw new Error("Failed to fetch book recent attempts");
      const data = await res.json();
      console.log(data);
      setQuizAttempts(data.content ?? data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [token]);

  return (
    <RecentAttemptsContext.Provider
      value={{
        userAttempts,
        bookAttempts,
        quizAttempts,
        loading,
        error,
        fetchUserRecentAttempts,
        fetchBookRecentAttempts,
        fetchQuizRecentAttempts,
      }}
    >
      {children}
    </RecentAttemptsContext.Provider>
  );
};

export const useRecentAttempts = () => {
  const context = useContext(RecentAttemptsContext);
  if (!context) {
    throw new Error("useRecentAttempts must be used within a RecentAttemptsProvider");
  }
  return context;
};
