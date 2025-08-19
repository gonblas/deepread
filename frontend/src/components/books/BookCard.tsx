import { useNavigate } from "react-router-dom";
import { BookOpen, Users, Tag } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { getGenreLabel, getGenreColor, type BookGenre } from "@/lib/genres";
import { EditBookDialog } from "./EditBookDialog";
import { CardBadge } from "../CardBadge";

export interface Book {
  id: string;
  title: string;
  description: string;
  genre: BookGenre;
  authors: string[];
}

interface BookCardProps {
  book: Book;
}

export function BookCard({ book }: BookCardProps) {
  const navigate = useNavigate();

  const handleOpenBook = () => {
    navigate(`/books/${book.id}`);
  };

  return (
    <Card className="group hover:shadow-lg transition-all duration-200 border-0 shadow-md h-full flex flex-col">
      <CardHeader className="pb-3 flex-shrink-0">
        <div className="flex items-start justify-between gap-2 mb-3">
          <CardBadge
            icon={Tag}
            text={getGenreLabel(book.genre)}
            className={`text-xs font-medium ${getGenreColor(book.genre)}`}
          />
        </div>

        <CardTitle
          className="text-lg leading-tight line-clamp-1 group-hover:text-primary transition-colors"
          title={book.title}
        >
          {book.title}
        </CardTitle>

        {book.authors.length > 0 ? (
          <div className="flex items-center gap-1 text-sm text-muted-foreground">
            <Users className="w-3 h-3 flex-shrink-0" />
            <span className="line-clamp-1" title={book.authors.join(", ")}>
              {book.authors.join(", ")}
            </span>
          </div>
        ) : (
          <div className="flex items-center gap-1 text-sm text-muted-foreground">
            <Users className="w-3 h-3 flex-shrink-0" />
            <span className="italic">No author specified</span>
          </div>
        )}
      </CardHeader>

      <CardContent className="pt-0 flex flex-col flex-grow">
        <div className="flex-grow">
          <CardDescription
            className="text-sm leading-relaxed line-clamp-4"
            title={book.description}
          >
            {book.description}
          </CardDescription>
        </div>

        <div className="mt-4 pt-4 border-t flex-shrink-0">
          <div className="flex gap-2">
            <Button className="flex-1 text-xs" onClick={handleOpenBook}>
              <BookOpen className="size-4 mr-1" />
              Open Book
            </Button>
            <EditBookDialog book={book} />
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
