"use client";

import { useEffect, useState } from "react";
import { ArrowLeft, BookOpen, Users, Tag } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { getGenreLabel, getGenreColor, type BookGenre } from "@/lib/genres";
import { EditBookDialog } from "../components/books/EditBookDialog";
import { useParams, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import { useAuth } from "@/contexts/authContext";
import { ChapterListSection } from "../components/books/ChapterListSection";
import { DeleteElementDialog } from "@/components/DeleteElementDialog";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";
import {
  RecentAttemptsProvider,
  useRecentAttempts,
} from "@/contexts/recentAttemptsContext";
import { SectionCards } from "@/components/statistics/SectionCards";
import { BarChartInteractive } from "@/components/statistics/BarChartInteractive";
import {
  StatisticsProvider,
  useStatistics,
} from "@/contexts/statisticsContext";
import { SectionHeader } from "@/components/SectionHeader";
import { AuthorsList } from "@/components/books/AuthorsList";

interface Book {
  id: string;
  title: string;
  description: string;
  genre: BookGenre | "";
  authors: string[];
}

interface Chapter {
  id: string;
  title: string;
  number: number;
  summary: string;
}

function BookComponent() {
  const { bookId } = useParams();
  const [book, setBook] = useState<Book>({
    id: "",
    title: "",
    description: "",
    genre: "",
    authors: [],
  });

  const {
    bookAttempts,
    fetchBookRecentAttempts,
    loading: loadingRecentAttempts,
    error: errorRecentAttempts,
  } = useRecentAttempts();

  const {
    bookStats,
    loading: statsLoading,
    error: statsError,
    fetchBookStats,
  } = useStatistics();

  const [chapters, setChapters] = useState<Chapter[]>([]);
  const navigate = useNavigate();
  const { logout } = useAuth();
  useEffect(() => {
    const fetchBookDetails = async () => {
      fetch(`http://localhost:8080/api/books/${bookId}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      })
        .then((response) => {
          if (response.status === 401) {
            logout();
            return;
          }
          if (response.status === 404) {
            navigate("/books");
            return;
          }
          return response.json();
        })
        .then((data) => {
          if (data) setBook(data);
        });

      fetch(`http://localhost:8080/api/books/${bookId}/chapters?sort=number`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      })
        .then((response) => response.json())
        .then((data) => {
          setChapters(data.content);
        })
        .catch((error) => {
          console.error("Error fetching chapters:", error);
        });
    };

    fetchBookDetails();
    if (bookId) {
      fetchBookStats(bookId);
      fetchBookRecentAttempts(bookId);
    }
  }, [bookId]);

  if (book.id === "") {
    return <div className="text-red-500">Book ID is missing</div>;
  }

  return (
    <article className="space-y-6">
      <nav className="flex items-center justify-between">
        <Button
          variant="ghost"
          className="gap-2"
          onClick={() => navigate("/books")}
        >
          <ArrowLeft className="h-4 w-4" />
          Back to Library
        </Button>

        <div className="flex items-center gap-2">
          <EditBookDialog book={book} />
          <DeleteElementDialog
            deleteURL={`http://localhost:8080/api/books/${bookId}`}
            redirectURL={`/books/`}
            resourceType="book"
            resourceName={book.title}
          />
        </div>
      </nav>

      <Card className="bg-gradient-to-r from-primary/10 via-primary/5 to-transparent">
        <div className="flex flex-col lg:flex-row gap-6 p-6">
          <div className="flex-shrink-0">
            <div className="w-48 h-64 bg-gradient-to-br from-primary/20 to-primary/10 rounded-lg border-2 border-primary/20 flex items-center justify-center">
              <BookOpen className="h-16 w-16 text-primary/40" />
            </div>
          </div>

          <div className="flex-1 space-y-4">
            <div>
              <div className="flex items-start justify-between mb-2">
                <h1 className="text-3xl font-bold tracking-tight">
                  {book.title}
                </h1>
                {book.genre !== "" && (
                  <Badge
                    variant="secondary"
                    className={`${getGenreColor(book.genre)} ml-4`}
                  >
                    <Tag className="w-3 h-3 mr-1" />
                    {getGenreLabel(book.genre)}
                  </Badge>
                )}
              </div>

              <div className="flex items-center gap-2 mb-4">
                <AuthorsList authors={book.authors} />
              </div>

              <p className="prose max-w-none text-muted-foreground leading-relaxed">
                {book.description}
              </p>
            </div>

            <div className="flex gap-4 my-auto">
              <div className="text-center p-3 bg-background/50 rounded-lg border">
                <span className="text-2xl font-bold text-primary">
                  {chapters.length}
                </span>
                <div className="text-xs text-muted-foreground">
                  Total Chapters
                </div>
              </div>
            </div>
          </div>
        </div>
      </Card>
      <ChapterListSection bookId={bookId} chapters={chapters} />
      {!statsLoading && !statsError && bookStats && (
        <div className="flex flex-col space-y-6 my-12">
          <SectionHeader
            title="Book Statistics"
            description="Overview of your book statistics and recent activity"
            loading={statsLoading}
            error={statsError}
          />
          <SectionCards stats={bookStats.stats} />
          <BarChartInteractive chartData={bookStats.dailyStatsTimeline} />
        </div>
      )}
      <RecentAttemptsTable
        recentAttempts={bookAttempts}
        loading={loadingRecentAttempts}
        error={errorRecentAttempts}
      />
    </article>
  );
}

export default function BookPage() {
  return (
    <StatisticsProvider>
      <RecentAttemptsProvider>
        <BookComponent />
      </RecentAttemptsProvider>
    </StatisticsProvider>
  );
}
