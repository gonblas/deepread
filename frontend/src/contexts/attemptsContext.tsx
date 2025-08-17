"use client"

import type React from "react"
import { createContext, useContext, useState, useCallback } from "react"
import { useAuth } from "./authContext"
import { useNotification } from "./notificationContext"
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

export type SortOption = "date-desc" | "date-asc" | "score-desc" | "score-asc"

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
  fetchAttempts: (page?: number, filters?: Partial<AttemptsFilters>) => Promise<void>
  setFilters: (filters: Partial<AttemptsFilters>) => void
  refreshAttempts: () => Promise<void>
}

const AttemptsContext = createContext<AttemptsContextValue | undefined>(undefined)

// Mock data for testing
const mockAttempts: QuizAttempt[] = [
  {
    id: "attempt-1",
    quiz_id: "quiz-1",
    answers: [{ correct: true }, { correct: true }, { correct: false }, { correct: true }, { correct: true }],
    correctCount: 4,
    startedAt: "2024-01-15T10:00:00Z",
    submittedAt: "2024-01-15T10:15:00Z",
    quiz: {
      title: "JavaScript Fundamentals",
      chapter: {
        title: "Chapter 1: Variables and Functions",
      },
    },
  },
  {
    id: "attempt-2",
    quiz_id: "quiz-2",
    answers: [{ correct: true }, { correct: false }, { correct: false }, { correct: true }],
    correctCount: 2,
    startedAt: "2024-01-14T14:30:00Z",
    submittedAt: "2024-01-14T14:45:00Z",
    quiz: {
      title: "React Hooks Deep Dive",
      chapter: {
        title: "Chapter 3: Advanced Hooks",
      },
    },
  },
  {
    id: "attempt-3",
    quiz_id: "quiz-3",
    answers: [
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: false },
      { correct: true },
    ],
    correctCount: 6,
    startedAt: "2024-01-13T09:15:00Z",
    submittedAt: "2024-01-13T09:30:00Z",
    quiz: {
      title: "CSS Grid and Flexbox",
      chapter: {
        title: "Chapter 2: Layout Systems",
      },
    },
  },
  {
    id: "attempt-4",
    quiz_id: "quiz-4",
    answers: [{ correct: false }, { correct: true }, { correct: false }],
    correctCount: 1,
    startedAt: "2024-01-12T16:20:00Z",
    submittedAt: "2024-01-12T16:35:00Z",
    quiz: {
      title: "Node.js Basics",
      chapter: {
        title: "Chapter 4: Server-side JavaScript",
      },
    },
  },
  {
    id: "attempt-5",
    quiz_id: "quiz-5",
    answers: [
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: true },
    ],
    correctCount: 6,
    startedAt: "2024-01-11T11:45:00Z",
    submittedAt: "2024-01-11T12:00:00Z",
    quiz: {
      title: "TypeScript Advanced Types",
      chapter: {
        title: "Chapter 5: Type System",
      },
    },
  },
  {
    id: "attempt-6",
    quiz_id: "quiz-6",
    answers: [
      { correct: true },
      { correct: false },
      { correct: true },
      { correct: true },
      { correct: false },
      { correct: true },
      { correct: false },
      { correct: true },
    ],
    correctCount: 5,
    startedAt: "2024-01-10T13:10:00Z",
    submittedAt: "2024-01-10T13:25:00Z",
    quiz: {
      title: "Database Design Principles",
      chapter: {
        title: "Chapter 6: Relational Databases",
      },
    },
  },
  {
    id: "attempt-7",
    quiz_id: "quiz-7",
    answers: [{ correct: true }, { correct: true }, { correct: false }],
    correctCount: 2,
    startedAt: "2024-01-09T15:30:00Z",
    submittedAt: "2024-01-09T15:45:00Z",
    quiz: {
      title: "API Design Best Practices",
      chapter: {
        title: "Chapter 7: RESTful Services",
      },
    },
  },
  {
    id: "attempt-8",
    quiz_id: "quiz-8",
    answers: [{ correct: true }, { correct: true }, { correct: true }, { correct: true }, { correct: false }],
    correctCount: 4,
    startedAt: "2024-01-08T08:45:00Z",
    submittedAt: "2024-01-08T09:00:00Z",
    quiz: {
      title: "Security Fundamentals",
      chapter: {
        title: "Chapter 8: Web Security",
      },
    },
  },
]

export function AttemptsProvider({ children }: { children: React.ReactNode }) {
  const [attempts, setAttempts] = useState<QuizAttempt[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [totalPages, setTotalPages] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [filters, setFiltersState] = useState<AttemptsFilters>({
    sortBy: "date-desc",
  })

  const { logout } = useAuth()
  const { showError } = useNotification()

  const sortAttempts = (attempts: QuizAttempt[], sortBy: SortOption): QuizAttempt[] => {
    return [...attempts].sort((a, b) => {
      switch (sortBy) {
        case "date-desc":
          return new Date(b.submittedAt).getTime() - new Date(a.submittedAt).getTime()
        case "date-asc":
          return new Date(a.submittedAt).getTime() - new Date(b.submittedAt).getTime()
        case "score-desc": {
          const scoreA = a.answers.length > 0 ? (a.correctCount / a.answers.length) * 100 : 0
          const scoreB = b.answers.length > 0 ? (b.correctCount / b.answers.length) * 100 : 0
          return scoreB - scoreA
        }
        case "score-asc": {
          const scoreA = a.answers.length > 0 ? (a.correctCount / a.answers.length) * 100 : 0
          const scoreB = b.answers.length > 0 ? (b.correctCount / b.answers.length) * 100 : 0
          return scoreA - scoreB
        }
        default:
          return 0
      }
    })
  }

  const filterAttempts = (attempts: QuizAttempt[], filters: AttemptsFilters): QuizAttempt[] => {
    let filtered = [...attempts]

    if (filters.startDate) {
      const startDate = new Date(filters.startDate)
      filtered = filtered.filter((attempt) => new Date(attempt.submittedAt) >= startDate)
    }

    if (filters.endDate) {
      const endDate = new Date(filters.endDate)
      endDate.setHours(23, 59, 59, 999) // Include the entire end date
      filtered = filtered.filter((attempt) => new Date(attempt.submittedAt) <= endDate)
    }

    return filtered
  }

  const fetchAttempts = useCallback(
    async (page = 0, newFilters?: Partial<AttemptsFilters>) => {
      setLoading(true)
      setError(null)

      const currentFilters = { ...filters, ...newFilters }

      try {
        // Simulate API call delay
        await new Promise((resolve) => setTimeout(resolve, 500))

        // For now, use mock data. In production, this would be a real API call
        const USE_MOCK_DATA = true

        if (USE_MOCK_DATA) {
          // Filter and sort mock data
          let filteredAttempts = filterAttempts(mockAttempts, currentFilters)
          filteredAttempts = sortAttempts(filteredAttempts, currentFilters.sortBy)

          // Simulate pagination
          const pageSize = 10
          const totalElements = filteredAttempts.length
          const totalPages = Math.ceil(totalElements / pageSize)
          const startIndex = page * pageSize
          const endIndex = startIndex + pageSize
          const paginatedAttempts = filteredAttempts.slice(startIndex, endIndex)

          setAttempts(paginatedAttempts)
          setTotalPages(totalPages)
          setCurrentPage(page)
        } else {
          // Real API call (commented out for now)
          const [sortField, sortOrder] = currentFilters.sortBy.split("-")
          const params = new URLSearchParams({
            page: page.toString(),
            size: "10",
            sort: `${sortField === "date" ? "submittedAt" : "correctCount"},${sortOrder}`,
          })

          if (currentFilters.startDate) {
            params.append("startDate", currentFilters.startDate)
          }
          if (currentFilters.endDate) {
            params.append("endDate", currentFilters.endDate)
          }

          const response = await fetch(`http://localhost:8080/api/attempts?${params.toString()}`, {
            headers: {
              Authorization: `Bearer ${Cookies.get("token")}`,
            },
          })

          if (!response.ok) {
            if (response.status === 401) {
              logout()
              return
            }
            throw new Error("Failed to fetch attempts")
          }

          const data: AttemptsResponse = await response.json()
          setAttempts(data.content)
          setTotalPages(data.totalPages)
          setCurrentPage(data.number)
        }

        if (newFilters) {
          setFiltersState(currentFilters)
        }
      } catch (err) {
        const errorMessage = err instanceof Error ? err.message : "Failed to fetch attempts"
        setError(errorMessage)
        showError("Error", errorMessage)
      } finally {
        setLoading(false)
      }
    },
    [filters, logout, showError],
  )

  const setFilters = useCallback(
    (newFilters: Partial<AttemptsFilters>) => {
      fetchAttempts(0, newFilters)
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
