"use client";

import { useState, useEffect, useRef } from "react";
import { Search, Brain } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { QuizCard } from "@/components/quizzes/QuizCard";
import { ErrorCard } from "@/components/ErrorCard";
import { Pagination } from "@/components/Pagination";
import { SearchSectionSkeleton } from "@/components/SearchSectionSkeleton";
import { SectionHeader } from "@/components/SectionHeader";
import { useAuth } from "@/contexts/authContext";
import { toast } from "sonner";
import Cookies from "js-cookie";
import { CardListContainer } from "@/components/CardListContainer";
import { SearchBox } from "@/components/search/SearchBox";
import InputField from "@/components/form/InputField";
import { useForm } from "@/hooks/useForm";
import { SearchNotFoundResourcesCard } from "@/components/SearchNotFoundResourcesCard";

interface Chapter {
  id: string;
  title: string;
  number: number;
  summary: string;
}

interface Quiz {
  id: string;
  chapter: Chapter;
  questions: Array<{
    id: string;
    type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN";
    prompt: string;
    explanation: string;
  }>;
}

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
  content: Quiz[];
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

export default function QuizzesPage() {
  const { values, handleChange, errors, resetForm } = useForm({
      search: "",
    });
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { logout } = useAuth();

  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);
  const maxTotalElements = 12;
  const hasActiveFilters = !!values.search;

  const fetchQuizzes = async (page = 0, search = "") => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        size: maxTotalElements.toString(),
        page: page.toString(),
      });

      if (search.trim()) {
        params.append("search", search.trim());
      }

      const response = await fetch(
        `http://localhost:8080/api/quizzes?${params.toString()}`,
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

      setQuizzes(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      setCurrentPage(data.number || 0);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "An error occurred while fetching books"
      );
      setQuizzes([]);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setCurrentPage(0);
  }, [values.search]);

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(() => {
      fetchQuizzes(currentPage, values.search);
    }, 500);

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, [currentPage, values.search]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage);
    }
  };

  const clearFilters = () => {
    resetForm();
    setCurrentPage(0);
  };

  const handleDeleteQuiz = async (quizId: string) => {
    try {
      // Simulate API call
      await new Promise((resolve) => setTimeout(resolve, 500));

      // Remove from local state
      setQuizzes((prev) => prev.filter((quiz) => quiz.id !== quizId));
      setTotalElements((prev) => prev - 1);

      toast.success("Quiz deleted successfully");
    } catch (err) {
      toast.error("Failed to delete quiz");
    }
  };

  return (
    <>
      <SectionHeader
        title="Quiz Library"
        loading={loading}
        error={error}
        description="Manage and review your educational quizzes"
      />

      <SearchBox
        hasActiveFilters={hasActiveFilters}
        onClearFilters={clearFilters}
        loading={loading}
        totalElements={totalElements}
        resourcesType="quizzes"
        icon={Brain}
      >
        <InputField
          label="Search quizzes"
          id="search"
          name="search"
          type="search"
          placeholder="Search by chapter title..."
          value={values.search}
          onChange={handleChange}
          error={errors.search}
          disabled={loading}
          className="w-full"
          icon={Search}
        />
      </SearchBox>

      <ErrorCard
        error={error}
        title="Error Loading Quizzes"
        onRetry={() => fetchQuizzes(currentPage, values.search)}
      />

      <SearchSectionSkeleton isLoading={loading} />

      <SearchNotFoundResourcesCard
        isEmpty={!loading && !error && quizzes.length === 0}
        resourceType="attempts"
        hasActiveFilters={hasActiveFilters}
        noItemsAdvice="Create your first quiz from a chapter to get started."
        icon={Brain}
      />

      <CardListContainer
        empty={quizzes.length === 0}
        loading={loading}
        error={!!error}
      >
        {quizzes.map((quiz) => (
          <QuizCard key={quiz.id} quiz={quiz} onDelete={handleDeleteQuiz} />
        ))}
      </CardListContainer>

      <Pagination
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={handlePageChange}
        maxVisiblePages={5}
        loading={loading}
        error={!!error}
      />
    </>
  );
}
