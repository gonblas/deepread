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
import { Calendar, ArrowUpDown } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Calendar as CalendarComponent } from "@/components/ui/calendar"
import { format } from "date-fns"
import { cn } from "@/lib/utils"
import { Label } from "@/components/ui/label";
import { SearchSectionSkeleton } from "@/components/SearchSectionSkeleton";

export type SortOption = "date-desc" | "date-asc" | "score-desc" | "score-asc"

const sortOptions: { value: SortOption; label: string }[] = [
  { value: "date-desc", label: "Newest First" },
  { value: "date-asc", label: "Oldest First" },
  { value: "score-desc", label: "Highest Score First" },
  { value: "score-asc", label: "Lowest Score First" },
]

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
      />

      <SearchBox
        hasActiveFilters={selectedSort !== "date-desc" || !!startDate || !!endDate}
        onClearFilters={clearFilters}
        loading={loading}
        totalElements={totalElements}
        resourcesType="quizzes"
        icon={Target}
      >
        <div className="w-full lg:w-64 space-y-2">
            <Label htmlFor="sort">
              Sort by
            </Label>
            <Select value={selectedSort} onValueChange={(value: SortOption) => setSelectedSort(value)} disabled={loading}>
              <SelectTrigger>
                <ArrowUpDown className="mr-2 h-4 w-4" />
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {sortOptions.map((option) => (
                  <SelectItem key={option.value} value={option.value}>
                    {option.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex-1 space-y-2">
            <Label>Start Date</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn("w-full justify-start text-left font-normal", !startDate && "text-muted-foreground")}
                  disabled={loading}
                >
                  <Calendar className="mr-2 h-4 w-4" />
                  {startDate ? format(startDate, "PPP") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <CalendarComponent
                  mode="single"
                  selected={startDate}
                  onSelect={setStartDate}
                  disabled={(date) => date > new Date() || (endDate ? date > endDate : false)}
                />
              </PopoverContent>
            </Popover>
          </div>

          <div className="flex-1 space-y-2">
            <Label>End Date</Label>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className={cn("w-full justify-start text-left font-normal", !endDate && "text-muted-foreground")}
                  disabled={loading}
                >
                  <Calendar className="mr-2 h-4 w-4" />
                  {endDate ? format(endDate, "PPP") : "Pick a date"}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <CalendarComponent
                  mode="single"
                  selected={endDate}
                  onSelect={setEndDate}
                  disabled={(date) => date > new Date() || (!!startDate && date < startDate)}
                />
              </PopoverContent>
            </Popover>
          </div>
      </SearchBox>

      <ErrorCard
        error={error}
        title="Error Loading Attempts"
        onRetry={() =>
          fetchAttempts(currentPage, selectedSort, startDate, endDate)
        }
      />

      <SearchSectionSkeleton isLoading={loading} />

      <SearchNotFoundResourcesCard
        isEmpty={!loading && !error && attempts.length === 0}
        resourceType="attempts"
        hasActiveFilters={!!hasActiveFilters}
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
