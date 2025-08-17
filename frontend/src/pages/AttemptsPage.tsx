"use client"

import { useState, useEffect, useRef } from "react"
import { Target } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { format } from "date-fns"
import Cookies from "js-cookie"
import { ErrorCard } from "@/components/ErrorCard"
import { Pagination } from "../components/Pagination";
import { SectionHeader } from "@/components/SectionHeader"
import { useAuth } from "@/contexts/authContext"
import { AttemptCard } from "@/components/attempts/AttemptCard"
import { AttemptsFilters, type SortOption } from "@/components/attempts/AttemptFilters"
import type { QuizAttempt } from "@/contexts/attemptsContext"

interface ApiResponse {
  totalElements: number
  totalPages: number
  pageable: {
    paged: boolean
    pageNumber: number
    pageSize: number
    unpaged: boolean
    offset: number
    sort: {
      sorted: boolean
      unsorted: boolean
      empty: boolean
    }
  }
  size: number
  content: QuizAttempt[]
  number: number
  sort: {
    sorted: boolean
    unsorted: boolean
    empty: boolean
  }
  first: boolean
  last: boolean
  numberOfElements: number
  empty: boolean
}

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
      title: "JavaScript Fundamentals and Advanced Concepts for Modern Web Development",
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
      title: "React Hooks Deep Dive: Understanding State Management and Side Effects",
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
      title: "CSS Grid and Flexbox Layout Systems for Responsive Design",
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
      title: "TypeScript Advanced Types and Generic Programming Patterns",
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
      title: "Database Design Principles and Normalization Techniques",
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
      title: "API Design Best Practices for RESTful Web Services and GraphQL",
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
  {
    id: "attempt-9",
    quiz_id: "quiz-9",
    answers: [{ correct: true }, { correct: true }, { correct: true }],
    correctCount: 3,
    startedAt: "2024-01-07T12:00:00Z",
    submittedAt: "2024-01-07T12:15:00Z",
    quiz: {
      title: "Docker and Containerization for Modern Application Deployment",
      chapter: {
        title: "Chapter 9: DevOps Fundamentals",
      },
    },
  },
  {
    id: "attempt-10",
    quiz_id: "quiz-10",
    answers: [{ correct: false }, { correct: true }, { correct: true }, { correct: false }, { correct: true }],
    correctCount: 3,
    startedAt: "2024-01-06T16:30:00Z",
    submittedAt: "2024-01-06T16:45:00Z",
    quiz: {
      title: "Machine Learning Basics and Python Data Science Libraries",
      chapter: {
        title: "Chapter 10: Data Science",
      },
    },
  },
  {
    id: "attempt-11",
    quiz_id: "quiz-11",
    answers: [{ correct: true }, { correct: true }, { correct: false }, { correct: true }],
    correctCount: 3,
    startedAt: "2024-01-05T10:20:00Z",
    submittedAt: "2024-01-05T10:35:00Z",
    quiz: {
      title: "Cloud Computing with AWS and Azure Services",
      chapter: {
        title: "Chapter 11: Cloud Platforms",
      },
    },
  },
  {
    id: "attempt-12",
    quiz_id: "quiz-12",
    answers: [
      { correct: true },
      { correct: false },
      { correct: true },
      { correct: true },
      { correct: true },
      { correct: false },
    ],
    correctCount: 4,
    startedAt: "2024-01-04T14:10:00Z",
    submittedAt: "2024-01-04T14:25:00Z",
    quiz: {
      title: "Microservices Architecture and Distributed Systems Design",
      chapter: {
        title: "Chapter 12: System Architecture",
      },
    },
  },
  {
    id: "attempt-13",
    quiz_id: "quiz-13",
    answers: [{ correct: true }, { correct: true }],
    correctCount: 2,
    startedAt: "2024-01-03T09:45:00Z",
    submittedAt: "2024-01-03T10:00:00Z",
    quiz: {
      title: "Agile Development Methodologies and Scrum Framework",
      chapter: {
        title: "Chapter 13: Project Management",
      },
    },
  },
  {
    id: "attempt-14",
    quiz_id: "quiz-14",
    answers: [{ correct: false }, { correct: true }, { correct: false }, { correct: true }, { correct: true }],
    correctCount: 3,
    startedAt: "2024-01-02T11:30:00Z",
    submittedAt: "2024-01-02T11:45:00Z",
    quiz: {
      title: "Mobile App Development with React Native and Flutter",
      chapter: {
        title: "Chapter 14: Mobile Development",
      },
    },
  },
  {
    id: "attempt-15",
    quiz_id: "quiz-15",
    answers: [{ correct: true }, { correct: true }, { correct: true }, { correct: false }],
    correctCount: 3,
    startedAt: "2024-01-01T15:20:00Z",
    submittedAt: "2024-01-01T15:35:00Z",
    quiz: {
      title: "Blockchain Technology and Cryptocurrency Fundamentals",
      chapter: {
        title: "Chapter 15: Emerging Technologies",
      },
    },
  },
]

export default function AttemptsPage() {
  const [selectedSort, setSelectedSort] = useState<SortOption>("date-desc")
  const [startDate, setStartDate] = useState<Date | undefined>(undefined)
  const [endDate, setEndDate] = useState<Date | undefined>(undefined)
  const [currentPage, setCurrentPage] = useState(0)
  const [attempts, setAttempts] = useState<QuizAttempt[]>([])
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const { logout } = useAuth()
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null)
  const maxTotalElements = 8

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

  const filterAttempts = (attempts: QuizAttempt[]): QuizAttempt[] => {
    let filtered = [...attempts]

    // Filter by date range
    if (startDate) {
      filtered = filtered.filter((attempt) => new Date(attempt.submittedAt) >= startDate)
    }

    if (endDate) {
      const endDateTime = new Date(endDate)
      endDateTime.setHours(23, 59, 59, 999)
      filtered = filtered.filter((attempt) => new Date(attempt.submittedAt) <= endDateTime)
    }

    return filtered
  }

  const fetchAttempts = async (page = 0, sort = selectedSort, startDateFilter?: Date, endDateFilter?: Date) => {
    setLoading(true)
    setError(null)

    try {
      // Simulate API call delay
      await new Promise((resolve) => setTimeout(resolve, 500))

      // For now, use mock data. In production, this would be a real API call
      const USE_MOCK_DATA = true

      if (USE_MOCK_DATA) {
        // Filter and sort mock data
        let filteredAttempts = filterAttempts(mockAttempts)
        filteredAttempts = sortAttempts(filteredAttempts, sort)

        // Simulate pagination
        const pageSize = maxTotalElements
        const totalElements = filteredAttempts.length
        const totalPages = Math.ceil(totalElements / pageSize)
        const startIndex = page * pageSize
        const endIndex = startIndex + pageSize
        const paginatedAttempts = filteredAttempts.slice(startIndex, endIndex)

        setAttempts(paginatedAttempts)
        setTotalPages(totalPages)
        setTotalElements(totalElements)
        setCurrentPage(page)
      } else {
        // Real API call (commented out for now)
        const [sortField, sortOrder] = sort.split("-")
        const params = new URLSearchParams({
          page: page.toString(),
          size: maxTotalElements.toString(),
          sort: `${sortField === "date" ? "submittedAt" : "correctCount"},${sortOrder}`,
        })

        if (startDateFilter) {
          params.append("startDate", format(startDateFilter, "yyyy-MM-dd"))
        }
        if (endDateFilter) {
          params.append("endDate", format(endDateFilter, "yyyy-MM-dd"))
        }

        const response = await fetch(`http://localhost:8080/api/attempts?${params.toString()}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        })

        if (!response.ok) {
          if (response.status === 401) {
            logout()
            return
          }
          throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data: ApiResponse = await response.json()
        setAttempts(data.content || [])
        setTotalPages(data.totalPages || 0)
        setTotalElements(data.totalElements || 0)
        setCurrentPage(data.number || 0)
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred while fetching attempts")
      setAttempts([])
      setTotalPages(0)
      setTotalElements(0)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    setCurrentPage(0)
  }, [selectedSort, startDate, endDate])

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current)
    }
    debounceTimeout.current = setTimeout(() => {
      fetchAttempts(currentPage, selectedSort, startDate, endDate)
    }, 300)

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current)
      }
    }
  }, [currentPage, selectedSort, startDate, endDate])

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage)
    }
  }

  const clearFilters = () => {
    setSelectedSort("date-desc")
    setStartDate(undefined)
    setEndDate(undefined)
    setCurrentPage(0)
  }

  const hasActiveFilters = selectedSort !== "date-desc" || startDate || endDate

  return (
    <div className="container mx-auto py-6 space-y-6">
      <SectionHeader
        title="Quiz Attempts"
        loading={loading}
        error={error}
        description="View and analyze your quiz performance history"
      >
        <div className="flex items-center gap-2 text-muted-foreground">
          <Target className="h-5 w-5" />
          <span>{loading ? "Loading..." : `${totalElements} attempts found`}</span>
        </div>
      </SectionHeader>

      <AttemptsFilters
        selectedSort={selectedSort}
        startDate={startDate}
        endDate={endDate}
        onSortChange={setSelectedSort}
        onStartDateChange={setStartDate}
        onEndDateChange={setEndDate}
        onClearFilters={clearFilters}
        loading={loading}
      />

      <ErrorCard
        error={error || ""}
        title="Error Loading Attempts"
        onRetry={() => fetchAttempts(currentPage, selectedSort, startDate, endDate)}
        retryButtonText="Try Again"
      />

      {loading && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {Array.from({ length: 8 }).map((_, i) => (
            <Card key={i} className="h-full">
              <CardContent className="p-6">
                <div className="animate-pulse space-y-4">
                  <div className="flex items-center justify-between">
                    <div className="h-4 bg-gray-200 rounded w-1/3"></div>
                    <div className="h-6 bg-gray-200 rounded w-16"></div>
                  </div>
                  <div className="h-6 bg-gray-200 rounded w-3/4"></div>
                  <div className="flex gap-4">
                    <div className="h-4 bg-gray-200 rounded w-24"></div>
                    <div className="h-4 bg-gray-200 rounded w-24"></div>
                  </div>
                  <div className="h-8 bg-gray-200 rounded w-full"></div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {!loading && !error && attempts.length === 0 && (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <Target className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="text-lg font-semibold mb-2">No attempts found</h3>
            <p className="text-muted-foreground text-center mb-4">
              {hasActiveFilters
                ? "No attempts match your search criteria. Try different terms or clear your filters."
                : "No quiz attempts available. Take your first quiz to get started."}
            </p>
          </CardContent>
        </Card>
      )}

      {!loading && !error && attempts.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {attempts.map((attempt) => (
            <AttemptCard key={attempt.id} attempt={attempt} />
          ))}
        </div>
      )}

      {!loading && !error && totalElements > maxTotalElements && totalPages > 1 && (
        <div className="flex justify-center">
          <Pagination currentPage={currentPage} totalPages={totalPages} onPageChange={handlePageChange} />
        </div>
      )}
    </div>
  )
}
