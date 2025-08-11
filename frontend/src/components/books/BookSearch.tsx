"use client"

import { useState, useEffect, useRef } from "react"
import { Search, Plus, Filter, BookOpen, ChevronLeft, ChevronRight } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { BookCard } from "@/components/books/BookCard"
import { getAllGenresWithLabels, getGenreLabel, type BookGenre } from "@/lib/genres"
import { Skeleton } from "@/components/ui/skeleton"
import Cookies from "js-cookie";

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

  const debounceTimeout = useRef<NodeJS.Timeout | null>(null)

  const fetchBooks = async (page = 0, search = "", genre = "all") => {
    setLoading(true)
    setError(null)

    try {
      const params = new URLSearchParams({
        size: "10",
        page: page.toString(),
      })

      if (genre !== "all") {
        params.append("genre", genre)
      }

      if (search.trim()) {
        params.append("search", search.trim())
      }

      const response = await fetch(`http://localhost:8080/api/book?${params.toString()}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      })

      if (!response.ok) {
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

  const handleOpenBook = (bookId: string) => {
    alert(`Opening book with ID: ${bookId}`)
  }

  const handleEditBook = (bookId: string) => {
    alert(`Editing book with ID: ${bookId}`)
  }

  const handleCreateBook = () => {
    alert("Navigating to create new book...")
  }

  const clearFilters = () => {
    setSearchTerm("")
    setSelectedGenre("all")
    setCurrentPage(0)
  }

  const getPageNumbers = () => {
    const pages = []
    const maxVisiblePages = 5
    let startPage = Math.max(0, currentPage - Math.floor(maxVisiblePages / 2))
    const endPage = Math.min(totalPages - 1, startPage + maxVisiblePages - 1)

    if (endPage - startPage < maxVisiblePages - 1) {
      startPage = Math.max(0, endPage - maxVisiblePages + 1)
    }

    for (let i = startPage; i <= endPage; i++) {
      pages.push(i)
    }

    return pages
  }

  return (
    <div className="space-y-6">
      <div className="space-y-4">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <div>
            <h1 className="text-3xl font-bold tracking-tight">Book Library</h1>
            <p className="text-muted-foreground">
              {loading
                ? "Loading books..."
                : error
                  ? "Error loading books"
                  : `Explore our collection of ${totalElements} available books`}
            </p>
          </div>
          <Button size="lg" className="shrink-0" onClick={handleCreateBook}>
            <Plus className="mr-2 h-5 w-5" />
            Create Book
          </Button>
        </div>

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
                  {loading ? "Loading..." : `${books.length} books found`}
                  {!loading && totalElements > 0 && (
                    <span className="ml-1">
                      (Page {currentPage + 1} of {totalPages})
                    </span>
                  )}
                </span>
                {searchTerm && !loading && (
                  <span>
                    Search: "<strong>{searchTerm}</strong>"
                  </span>
                )}
                {selectedGenre !== "all" && !loading && (
                  <span>
                    Genre: <strong>{getGenreLabel(selectedGenre as BookGenre)}</strong>
                  </span>
                )}
              </div>

              {(searchTerm || selectedGenre !== "all") && !loading && (
                <Button variant="ghost" size="sm" onClick={clearFilters}>
                  Clear filters
                </Button>
              )}
            </div>
          </CardContent>
        </Card>
      </div>

      {error && (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <div className="text-red-500 mb-4">⚠️</div>
            <h3 className="text-lg font-semibold mb-2 text-red-600">Error Loading Books</h3>
            <p className="text-muted-foreground text-center mb-4">{error}</p>
            <Button onClick={() => fetchBooks(currentPage, searchTerm, selectedGenre)}>Try Again</Button>
          </CardContent>
        </Card>
      )}

      {loading && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {Array.from({ length: 8 }).map((_, index) => (
            <Card key={index} className="h-full flex flex-col">
              <div className="p-6 pb-3 flex-shrink-0">
                <Skeleton className="h-4 w-20 mb-3" />
                <Skeleton className="h-6 w-full mb-2" />
                <Skeleton className="h-4 w-3/4" />
              </div>
              <div className="px-6 pb-6 flex flex-col flex-grow">
                <div className="flex-grow">
                  <Skeleton className="h-4 w-full mb-2" />
                  <Skeleton className="h-4 w-full mb-2" />
                  <Skeleton className="h-4 w-2/3" />
                </div>
                <div className="mt-4 pt-4 border-t flex-shrink-0">
                  <div className="flex gap-2">
                    <Skeleton className="h-8 flex-1" />
                    <Skeleton className="h-8 flex-1" />
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}

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
            <BookCard key={book.id} book={book} onOpenBook={handleOpenBook} onEditBook={handleEditBook} />
          ))}
        </div>
      )}

      {!loading && !error && totalElements > 10 && totalPages > 1 && (
        <div className="flex justify-center pt-6">
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 0}
            >
              <ChevronLeft className="h-4 w-4 mr-1" />
              Previous
            </Button>

            <div className="flex items-center gap-1">
              {getPageNumbers().map((pageNum) => (
                <Button
                  key={pageNum}
                  variant={currentPage === pageNum ? "default" : "outline"}
                  size="sm"
                  onClick={() => handlePageChange(pageNum)}
                  className="min-w-[40px]"
                >
                  {pageNum + 1}
                </Button>
              ))}
            </div>

            <Button
              variant="outline"
              size="sm"
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages - 1}
            >
              Next
              <ChevronRight className="h-4 w-4 ml-1" />
            </Button>
          </div>
        </div>
      )}
    </div>
  )
}
