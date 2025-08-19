"use client";

import { useState, useEffect, useRef } from "react";
import { Target } from "lucide-react";
import Cookies from "js-cookie";
import { ErrorCard } from "@/components/ErrorCard";
import { Pagination } from "../components/Pagination";
import { SectionHeader } from "@/components/SectionHeader";
import { useAuth } from "@/contexts/authContext";
import { AttemptCard } from "@/components/attempts/AttemptCard";
import {
  AttemptsProvider,
  useAttempts,
  type QuizAttempt,
} from "@/contexts/attemptsContext";
import { CardListContainer } from "@/components/CardListContainer";
import { SearchNotFoundResourcesCard } from "@/components/SearchNotFoundResourcesCard";
import { SearchBox } from "@/components/search/SearchBox";
import { ArrowUpDown } from "lucide-react";
import { SearchSectionSkeleton } from "@/components/SearchSectionSkeleton";
import { useForm } from "@/hooks/useForm";
import SelectField from "@/components/form/SelectField";
import DateField from "@/components/form/DateField";

export type SortOption = "date-desc" | "date-asc" | "score-desc" | "score-asc";

const sortOptions: { value: SortOption; label: string }[] = [
  { value: "date-desc", label: "Newest First" },
  { value: "date-asc", label: "Oldest First" },
  { value: "score-desc", label: "Highest Score First" },
  { value: "score-asc", label: "Lowest Score First" },
];

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
  const { values, handleChange, errors, resetForm } = useForm({
    sort: "date-desc",
    startDate: undefined,
    endDate: undefined,
  });

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const [attempts, setAttempts] = useState<QuizAttempt[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { sortMap } = useAttempts();

  const { logout } = useAuth();
  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);
  const maxTotalElements = 12;
  const hasActiveFilters =
    values.sort !== "date-desc" || !!values.startDate || !!values.endDate;

  const fetchAttempts = async (
    page = 0,
    sort = values.sort,
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
          startDateFilter.toISOString()
        );
      }

      if (endDateFilter) {
        const endOfDay = new Date(endDateFilter);
        endOfDay.setHours(23, 59, 59, 0);
        params.append("submittedTo", endOfDay.toISOString());
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
  }, [values.sort, values.startDate, values.endDate]);

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(() => {
      fetchAttempts(currentPage, values.sort, values.startDate, values.endDate);
    }, 300);

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, [currentPage, values.sort, values.startDate, values.endDate]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage);
    }
  };

  const clearFilters = () => {
    resetForm();
    setCurrentPage(0);
  };

  return (
    <div className="container mx-auto py-6 space-y-6">
      <SectionHeader
        title="Quiz Attempts"
        loading={loading}
        error={error}
        description="View and analyze your quiz performance history"
      />

      <SearchBox
        hasActiveFilters={hasActiveFilters}
        onClearFilters={clearFilters}
        loading={loading}
        totalElements={totalElements}
        resourcesType="quizzes"
        icon={Target}
      >
        <SelectField
          label="Sort by"
          id="sort"
          name="sort"
          value={values.sort}
          onValueChange={(value) =>
            handleChange({ target: { name: "sort", value } } as any)
          }
          required
          error={errors.sort}
          options={sortOptions}
          icon={ArrowUpDown}
        />

        <DateField
          label="Start Date"
          id="startDate"
          name="startDate"
          value={values.startDate}
          onValueChange={(date) =>
            handleChange({ target: { name: "startDate", value: date } } as any)
          }
          error={errors.startDate}
          required
          maxDate={values.endDate}
        />

        <DateField
          label="End Date"
          id="endDate"
          name="endDate"
          value={values.endDate}
          onValueChange={(date) =>
            handleChange({ target: { name: "endDate", value: date } } as any)
          }
          error={errors.endDate}
          required
          minDate={values.startDate}
          maxDate={new Date()}
        />
      </SearchBox>

      <ErrorCard
        error={error}
        title="Error Loading Attempts"
        onRetry={() =>
          fetchAttempts(currentPage, values.sort, values.startDate, values.endDate)
        }
      />

      <SearchSectionSkeleton isLoading={loading} />

      <SearchNotFoundResourcesCard
        isEmpty={!loading && !error && attempts.length === 0}
        resourceType="attempts"
        hasActiveFilters={hasActiveFilters}
        noItemsAdvice="Take your first quiz to get started."
        icon={Target}
      />

      <CardListContainer
        empty={attempts.length === 0}
        loading={loading}
        error={!!error}
      >
        {attempts.map((attempt) => (
          <AttemptCard key={attempt.id} attempt={attempt} />
        ))}
      </CardListContainer>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
        loading={loading}
        error={!!error}
      />
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
