"use client"

import { useState, useEffect, useRef } from "react"
import { Search, Plus, Filter, BookOpen, Book } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { BookCard } from "@/components/books/BookCard"
import { getAllGenresWithLabels, type BookGenre } from "@/lib/genres"
import Cookies from "js-cookie";
import { ErrorCard } from "../ErrorCard"
import { Pagination } from "../Pagination"
import { BookSectionSkeleton } from "./BookSectionSkeleton"
import { useAuth } from "@/contexts/authContext"
import { SectionHeader } from "../SectionHeader"

interface Book {
  id: string
  title: string
  description: string
  genre: BookGenre
  authors: string[]
}

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
  content: Book[]
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

export function BooksSearch() {
  const [searchTerm, setSearchTerm] = useState("")
  const [selectedGenre, setSelectedGenre] = useState<string>("all")
  const [currentPage, setCurrentPage] = useState(0)
  const [books, setBooks] = useState<Book[]>([])
  const [totalPages, setTotalPages] = useState(0)
  const [totalElements, setTotalElements] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const genresWithLabels = getAllGenresWithLabels()
  const { logout } = useAuth()

  const debounceTimeout = useRef<NodeJS.Timeout | null>(null)
  const maxTotalElements = 12 
  const fetchBooks = async (page = 0, search = "", genre = "all") => {
    setLoading(true)
    setError(null)

    try {
      const params = new URLSearchParams({
        size: maxTotalElements.toString(),
        page: page.toString(),
      })

      if (genre !== "all") {
        params.append("genres", genre)
      }

      if (search.trim()) {
        params.append("search", search.trim())
      }

      console.log(`Fetching books with params: http://localhost:8080/api/book?${params.toString()}`)
      const response = await fetch(`http://localhost:8080/api/book?${params.toString()}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      })

      if (!response.ok) {
        if(response.status === 401) {
          logout()
          return
        }
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const data: ApiResponse = await response.json()

      setBooks(data.content || [])
      setTotalPages(data.totalPages || 0)
      setTotalElements(data.totalElements || 0)
      setCurrentPage(data.number || 0)
    } catch (err) {
      setError(err instanceof Error ? err.message : "An error occurred while fetching books")
      setBooks([])
      setTotalPages(0)
      setTotalElements(0)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    setCurrentPage(0)
  }, [searchTerm, selectedGenre])

  useEffect(() => {
    if (debounceTimeout.current) {
      clearTimeout(debounceTimeout.current)
    }
    debounceTimeout.current = setTimeout(() => {
      fetchBooks(currentPage, searchTerm, selectedGenre)
    }, 500)

    return () => {
      if (debounceTimeout.current) {
        clearTimeout(debounceTimeout.current)
      }
    }
  }, [currentPage, searchTerm, selectedGenre])

  const handlePageChange = (newPage: number) => {
    if (newPage >= 0 && newPage < totalPages && newPage !== currentPage) {
      setCurrentPage(newPage)
    }
  }

  const handleCreateBook = () => {
    alert("Navigating to create new book...")
  }

  const clearFilters = () => {
    setSearchTerm("")
    setSelectedGenre("all")
    setCurrentPage(0)
  }

  return (
    <>
      <>
        <SectionHeader 
          title="Book Library"
          loading={loading}
          error={error}
          description="Explore our collection of available books"
          buttonText="Create Book"
          onButtonClick={handleCreateBook}
          buttonIcon={<Plus className="mr-1 size-6" />}
        />
          
        <Card>
          <CardContent className="pt-6">
            <div className="flex flex-col lg:flex-row gap-4">
              <div className="flex-1 space-y-2">
                <Label htmlFor="search" className="text-sm font-medium">
                  Search books
                </Label>
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                  <Input
                    id="search"
                    placeholder="Search by title or author..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="pl-10"
                    disabled={loading}
                  />
                </div>
              </div>

              <div className="w-full lg:w-64 space-y-2">
                <Label htmlFor="genre" className="text-sm font-medium">
                  Filter by genre
                </Label>
                <Select value={selectedGenre} onValueChange={setSelectedGenre} disabled={loading}>
                  <SelectTrigger>
                    <Filter className="mr-2 h-4 w-4" />
                    <SelectValue placeholder="All genres" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All genres</SelectItem>
                    {genresWithLabels.map((genre) => (
                      <SelectItem key={genre.value} value={genre.value}>
                        {genre.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            </div>

            <div className="flex items-center justify-between mt-4 pt-4 border-t">
              <div className="flex items-center gap-4 text-sm text-muted-foreground">
                <span className="flex items-center gap-1">
                  <BookOpen className="h-4 w-4" />
                  {loading ? "Loading..." : `${totalElements} books found`}
                </span>
              </div>

              {(searchTerm || selectedGenre !== "all") && !loading && (
                <Button variant="ghost" size="sm" onClick={clearFilters}>
                  Clear filters
                </Button>
              )}
            </div>
          </CardContent>
        </Card>
      </>

      <ErrorCard
        error={error || ""}
        title="Error Loading Books"
        onRetry={() => fetchBooks(currentPage, searchTerm, selectedGenre)}
        retryButtonText="Try Again"
      />

      <BookSectionSkeleton isLoading={loading} />

      {!loading && !error && books.length === 0 && (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <BookOpen className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="text-lg font-semibold mb-2">No books found</h3>
            <p className="text-muted-foreground text-center mb-4">
              {searchTerm || selectedGenre !== "all"
                ? "No books match your search criteria. Try different terms or create a new book."
                : "No books available. Create your first book to get started."}
            </p>
            <Button onClick={handleCreateBook}>
              <Plus className="mr-2 h-4 w-4" />
              {totalElements === 0 ? "Create First Book" : "Create New Book"}
            </Button>
          </CardContent>
        </Card>
      )}

      {!loading && !error && books.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {books.map((book) => (
            <BookCard key={book.id} book={book}/>
          ))}
        </div>
      )}

      {!loading && !error && totalElements > maxTotalElements && totalPages > 1 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={handlePageChange}
          maxVisiblePages={5}
        />
      )}
    </>
  )
}
