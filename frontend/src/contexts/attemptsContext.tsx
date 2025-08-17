"use client"

import type React from "react"
import { createContext, useContext, useState, useCallback } from "react"
import { useAuth } from "./authContext"
import Cookies from "js-cookie"

export interface QuizAttempt {
  id: string
  quiz_id: string
  answers: Array<{ correct: boolean }>
  correctCount: number
  startedAt: string
  submittedAt: string
  quiz?: {
    title?: string
    chapter?: {
      title?: string
    }
  }
}

export interface AttemptsResponse {
  totalElements: number
  totalPages: number
  content: QuizAttempt[]
  number: number
  size: number
  first: boolean
  last: boolean
  empty: boolean
}

export type SortOption = "date-desc" | "date-asc" | "score-desc" | "score-asc";

export const sortMap: Record<SortOption, string> = {
  "date-desc": "submittedAt,desc",
  "date-asc": "submittedAt,asc",
  "score-desc": "correctCount,desc",
  "score-asc": "correctCount,asc",
};


export interface AttemptsFilters {
  startDate?: string
  endDate?: string
  sortBy: SortOption
}

interface AttemptsContextValue {
  attempts: QuizAttempt[]
  loading: boolean
  error: string | null
  totalPages: number
  currentPage: number
  filters: AttemptsFilters
  fetchAttempts: (page?: number) => Promise<void>
  setFilters: (filters: Partial<AttemptsFilters>) => void
  refreshAttempts: () => Promise<void>
  sortMap: Record<SortOption, string>
}

const AttemptsContext = createContext<AttemptsContextValue | undefined>(undefined)

export function AttemptsProvider({ children }: { children: React.ReactNode }) {
  const [attempts, setAttempts] = useState<QuizAttempt[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const maxTotalElements = 12;
  const [filters, setFiltersState] = useState<AttemptsFilters>({
    sortBy: "date-desc",
  })

  const { logout } = useAuth()

  const fetchAttempts = async (page = 0) => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        size: maxTotalElements.toString(),
        page: page.toString(),
      });

      const response = await fetch(
        `http://localhost:8080/api/attempts?${params.toString()}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          logout();
          return;
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      setFiltersState((prev) => ({ ...prev, ...filters }));
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "An error occurred while fetching books"
      );
      setAttempts([]);
      setTotalPages(0);
      setCurrentPage(0);
    } finally {
      setLoading(false);
    }
  };

  const setFilters = useCallback(
    () => {
      fetchAttempts(0)
    },
    [fetchAttempts],
  )

  const refreshAttempts = useCallback(() => {
    return fetchAttempts(currentPage)
  }, [fetchAttempts, currentPage])

  const value: AttemptsContextValue = {
    attempts,
    loading,
    error,
    totalPages,
    currentPage,
    filters,
    fetchAttempts,
    setFilters,
    refreshAttempts,
    sortMap,
  }

  return <AttemptsContext.Provider value={value}>{children}</AttemptsContext.Provider>
}

export function useAttempts() {
  const context = useContext(AttemptsContext)
  if (context === undefined) {
    throw new Error("useAttempts must be used within an AttemptsProvider")
  }
  return context
}
