"use client";

import { useState, useEffect, useRef } from "react";
import { Search, Plus, Filter, Book, BookDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import { BOOK_GENRES, getGenreLabel } from "@/lib/genres";
import { BookCard } from "@/components/books/BookCard";
import { type BookGenre } from "@/lib/genres";
import Cookies from "js-cookie";
import { ErrorCard } from "../components/ErrorCard";
import { Pagination } from "../components/Pagination";
import { SearchSectionSkeleton } from "../components/SearchSectionSkeleton";
import { useAuth } from "@/contexts/authContext";
import { SectionHeader } from "../components/SectionHeader";
import { useNavigate } from "react-router-dom";
import { CardListContainer } from "@/components/CardListContainer";
import { SearchNotFoundResourcesCard } from "@/components/SearchNotFoundResourcesCard";
import { SearchBox } from "@/components/search/SearchBox";
import { useForm } from "@/hooks/useForm";
import InputField from "@/components/form/InputField";
import SelectField from "@/components/form/SelectField";

interface Book {
  id: string;
  title: string;
  description: string;
  genre: BookGenre;
  authors: string[];
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
  content: Book[];
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

export default function BookListPage() {
  const { values, handleChange, errors, resetForm } = useForm({
    search: "",
    genres: "",
  });

  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [books, setBooks] = useState<Book[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { logout } = useAuth();
  const navigate = useNavigate();

  const debounceTimeout = useRef<NodeJS.Timeout | null>(null);
  const maxTotalElements = 12;
  const hasActiveFilters = !!values.search || !!values.genres;
  const fetchBooks = async (page = 0, search = "", genre = "all") => {
    setLoading(true);
    setError(null);

    try {
      const params = new URLSearchParams({
        size: maxTotalElements.toString(),
        page: page.toString(),
      });

      if (genre.length > 0) {
        params.append("genres", genre);
      }

      if (search.trim()) {
        params.append("search", search.trim());
      }

      const response = await fetch(
        `http://localhost:8080/api/books?${params.toString()}`,
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

      setBooks(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
      setCurrentPage(data.number || 0);
    } catch (err) {
      setError(
        err instanceof Error
          ? err.message
          : "An error occurred while fetching books"
      );
      setBooks([]);
      setTotalPages(0);
      setTotalElements(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setCurrentPage(0);
  }, [values.search, values.genres]);

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current);
    }
    debounceTimeout.current = setTimeout(() => {
      fetchBooks(currentPage, values.search, values.genres);
    }, 500);

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current);
      }
    };
  }, [currentPage, values.search, values.genres]);

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage);
    }
  };

  const handleCreateBook = () => {
    navigate("/books/create");
  };

  const clearFilters = () => {
    resetForm();
    setCurrentPage(0);
  };

  return (
    <>
      <SectionHeader
        title="Book Library"
        loading={loading}
        error={error}
        description="Explore our collection of available books"
      >
        <Button size="lg" className="shrink-0" onClick={handleCreateBook}>
          <span className="mr-1 size-4 flex items-center">
            <Plus className="mr-1 size-6" />
          </span>
          Create Book
        </Button>
      </SectionHeader>

      <SearchBox
        hasActiveFilters={hasActiveFilters}
        onClearFilters={clearFilters}
        loading={loading}
        totalElements={totalElements}
        resourcesType="books"
        icon={Book}
      >
        <div className="grid gap-4 grid-rows-1 lg:grid-cols-[75%_23.5%] w-full">
          <InputField
            label="Search books"
            id="search"
            name="search"
            type="search"
            placeholder="Search by title or author..."
            value={values.search}
            onChange={handleChange}
            error={errors.search}
            disabled={loading}
            className="w-full"
            icon={Search}
          />

          <SelectField
            label="Filter by genre"
            id="genre"
            name="genre"
            value={values.genres}
            onValueChange={(value) =>
              handleChange({ target: { name: "genres", value } } as any)
            }
            required
            error={errors.genres}
            options={BOOK_GENRES.map((genre) => ({
              value: genre,
              label: getGenreLabel(genre),
            }))}
            icon={Filter}
          />
        </div>
      </SearchBox>

      <ErrorCard
        error={error}
        title="Error Loading Books"
        onRetry={() => fetchBooks(currentPage, values.search, values.genres)}
      />

      <SearchSectionSkeleton isLoading={loading} />

      <SearchNotFoundResourcesCard
        isEmpty={!loading && !error && books.length === 0}
        resourceType="books"
        hasActiveFilters={hasActiveFilters}
        noItemsAdvice="Create your first book to get started."
        icon={BookDown}
        callToAction={
          <Button onClick={handleCreateBook}>
            <Plus className="mr-2 h-4 w-4" />
            {totalElements === 0 ? "Create First Book" : "Create New Book"}
          </Button>
        }
      />

      <CardListContainer
        empty={books.length === 0}
        loading={loading}
        error={!!error}
      >
        {books.map((book) => (
          <BookCard key={book.id} book={book} />
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
