"use client";

import { useEffect, useState } from "react";
import { ArrowLeft, BookOpen, Users, Tag } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { getGenreLabel, getGenreColor, type BookGenre } from "@/lib/genres";
import { EditBookDialog } from "./EditBookDialog";
import { useParams, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import { DeleteBookDialog } from "./DeleteBookDialog";
import { useAuth } from "@/contexts/authContext";
import { ChapterListSection } from "./ChapterListSection";

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

export function BookDetails() {
  const { bookId } = useParams();
  const [book, setBook] = useState<Book>({
    id: "",
    title: "",
    description: "",
    genre: "",
    authors: [],
  });
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

      fetch(`http://localhost:8080/api/books/${bookId}/chapters`, {
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
          <DeleteBookDialog bookTitle={book.title} bookId={bookId} />
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
                <Users className="h-4 w-4 text-muted-foreground" />
                <div className="flex flex-wrap gap-2">
                  {book.authors.length > 0 ? (
                    book.authors.map((author, index) => (
                      <div key={index} className="flex items-center gap-2">
                        <span className="text-sm font-medium">{author}</span>
                        {index < book.authors.length - 1 && (
                          <span className="text-muted-foreground">•</span>
                        )}
                      </div>
                    ))
                  ) : (
                    <span className="text-sm text-muted-foreground italic">
                      No authors specified
                    </span>
                  )}
                </div>
              </div>

              <p className="prose max-w-none text-muted-foreground leading-relaxed">
                {book.description}
              </p>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 pt-4">
              <div className="text-center p-3 bg-background/50 rounded-lg border">
                <div className="text-2xl font-bold text-primary">
                  {chapters.length}
                </div>
                <div className="text-xs text-muted-foreground">
                  Total Chapters
                </div>
              </div>
            </div>
          </div>
        </div>
      </Card>

      <ChapterListSection bookId={bookId} chapters={chapters} />
    </article>
  );
}
