"use client";

import { useEffect, useState } from "react";
import {
  ArrowLeft,
  Edit,
  Trash2,
  Plus,
  BookOpen,
  Users,
  Tag,
  Eye,
  FileText,
  MoreHorizontal,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { getGenreLabel, getGenreColor, type BookGenre } from "@/lib/genres";
import { EditBookDialog } from "./EditBookDialog";
import { useParams, useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import { DeleteBookDialog } from "./DeleteBookDialog";
import { useAuth } from "@/contexts/authContext";

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
      fetch(`http://localhost:8080/api/book/${bookId}`, {
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

      fetch(`http://localhost:8080/api/book/${bookId}/chapters`, {
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
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <Button variant="ghost" className="gap-2">
          <ArrowLeft className="h-4 w-4" />
          Back to Library
        </Button>

        <div className="flex items-center gap-2">
          <EditBookDialog book={book} />
          <DeleteBookDialog bookTitle={book.title} bookId={bookId} />
        </div>
      </div>

      <Card className="overflow-hidden">
        <div className="bg-gradient-to-r from-primary/10 via-primary/5 to-transparent p-6">
          <div className="flex flex-col lg:flex-row gap-6">
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

                <div className="prose max-w-none">
                  <p className="text-muted-foreground leading-relaxed">
                    {book.description}
                  </p>
                </div>
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
        </div>
      </Card>

      {/* Chapters Section */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="flex items-center gap-2">
                <FileText className="h-5 w-5" />
                Chapters ({chapters.length})
              </CardTitle>
              <CardDescription>
                Manage and organize your book chapters
              </CardDescription>
            </div>
            <Button
              //onClick={handleAddChapter}
              className="gap-2"
            >
              <Plus className="h-4 w-4" />
              Add Chapter
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          {chapters.length === 0 ? (
            <div className="text-center py-12">
              <FileText className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
              <h3 className="text-lg font-semibold mb-2">No chapters yet</h3>
              <p className="text-muted-foreground mb-4">
                Start writing by adding your first chapter
              </p>
              <Button
                //onClick={handleAddChapter}
                className="gap-2"
              >
                <Plus className="h-4 w-4" />
                Add First Chapter
              </Button>
            </div>
          ) : (
            <div className="space-y-3">
              {chapters.map((chapter, index) => (
                <div key={chapter.id}>
                  <div className="flex items-center justify-between p-4 rounded-lg border hover:bg-accent/50 transition-colors">
                    <div className="flex items-center gap-4">
                      <div className="flex items-center justify-center w-8 h-8 rounded-full bg-primary/10 text-primary font-semibold text-sm">
                        {chapter.number}
                      </div>
                      <div className="flex-1">
                        <h4 className="font-semibold">{chapter.title}</h4>
                      </div>
                    </div>

                    <div className="flex items-center gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        //onClick={() => handleViewChapter(chapter.id)}
                        className="gap-1"
                      >
                        <Eye className="h-3 w-3" />
                        View
                      </Button>

                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button variant="ghost" size="sm">
                            <MoreHorizontal className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuLabel>Chapter Actions</DropdownMenuLabel>
                          <DropdownMenuItem
                          //onClick={() => handleEditChapter(chapter.id)}
                          >
                            <Edit className="mr-2 h-4 w-4" />
                            Edit Chapter
                          </DropdownMenuItem>
                          <DropdownMenuItem>
                            <FileText className="mr-2 h-4 w-4" />
                            Duplicate
                          </DropdownMenuItem>
                          <DropdownMenuSeparator />
                          <DropdownMenuItem className="text-red-600">
                            <Trash2 className="mr-2 h-4 w-4" />
                            Delete Chapter
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </div>
                  </div>
                  {index < chapters.length - 1 && (
                    <Separator className="my-2" />
                  )}
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
