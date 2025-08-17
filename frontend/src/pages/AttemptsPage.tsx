"use client";

import { useState, useEffect, useRef } from "react";
import { Target } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import Cookies from "js-cookie";
import { ErrorCard } from "@/components/ErrorCard";
import { Pagination } from "../components/Pagination";
import { SectionHeader } from "@/components/SectionHeader";
import { useAuth } from "@/contexts/authContext";
import { AttemptCard } from "@/components/attempts/AttemptCard";
import {
  AttemptsFilters,
  type SortOption,
} from "@/components/attempts/AttemptFilters";
import {
  AttemptsProvider,
  useAttempts,
  type QuizAttempt,
} from "@/contexts/attemptsContext";

interface ApiResponse {
  totalElements: number;
  totalPages: number;
  pageable: {
    paged: boolean;
    pageNumber: number;
    pageSize: number;
    unpaged: boolean;
    offset: number;
    sort: {
      sorted: boolean;
      unsorted: boolean;
      empty: boolean;
    };
  };
  size: number;
  content: QuizAttempt[];
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

function AttemptsComponent() {
  const [selectedSort, setSelectedSort] = useState<SortOption>("date-desc");
  const [startDate, setStartDate] = useState<Date | undefined>(undefined);
  const [endDate, setEndDate] = useState<Date | undefined>(undefined);
  const [currentPage, setCurrentPage] = useState(0);
  const [attempts, setAttempts] = useState<QuizAttempt[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { sortMap } = useAttempts();

  const { logout } = useAuth();
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);
  const maxTotalElements = 12;

  const fetchAttempts = async (
    page = 0,
    sort = selectedSort,
    startDateFilter?: Date,
    endDateFilter?: Date
  ) => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        size: maxTotalElements.toString(),
        page: page.toString(),
      });

      if (sort) {
        params.append("sort", sortMap[sort]);
      }

      if (startDateFilter) {
        params.append(
          "submittedFrom",
          startDateFilter.toISOString().slice(0, 19)
        );
      }

      if (endDateFilter) {
        const endOfDay = new Date(endDateFilter);
        endOfDay.setHours(23, 59, 59, 0);
        params.append("submittedTo", endOfDay.toISOString().slice(0, 19));
      }

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

      const data: ApiResponse = await response.json();

      setAttempts(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
      setCurrentPage(data.number);
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

  useEffect(() => {
    setCurrentPage(0);
  }, [selectedSort, startDate, endDate]);

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(() => {
      fetchAttempts(currentPage, selectedSort, startDate, endDate);
    }, 300);

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, [currentPage, selectedSort, startDate, endDate]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage);
    }
  };

  const clearFilters = () => {
    setSelectedSort("date-desc");
    setStartDate(undefined);
    setEndDate(undefined);
    setCurrentPage(0);
  };

  const hasActiveFilters = selectedSort !== "date-desc" || startDate || endDate;

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
          <span>
            {loading ? "Loading..." : `${totalElements} attempts found`}
          </span>
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
        onRetry={() =>
          fetchAttempts(currentPage, selectedSort, startDate, endDate)
        }
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

      {!loading &&
        !error &&
        totalElements > maxTotalElements &&
        totalPages > 1 && (
          <div className="flex justify-center">
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={handlePageChange}
            />
          </div>
        )}
    </div>
  );
}

export default function AttemptsPage() {
  return (
    <AttemptsProvider>
      <AttemptsComponent />
    </AttemptsProvider>
  );
}
