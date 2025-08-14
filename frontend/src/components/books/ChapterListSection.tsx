
import {
  Plus,
  Eye,
  FileText,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { useNavigate } from "react-router-dom";

function NoChaptersPlaceholder({ bookId }: { bookId: string | undefined }) {
  const navigate = useNavigate();

  return (
    <div className="text-center py-12">
      <FileText className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
      <h3 className="text-lg font-semibold mb-2">No chapters yet</h3>
      <p className="text-muted-foreground mb-4">
        Start writing by adding your first chapter
      </p>
      <Button
        onClick={() => navigate(`/books/${bookId}/chapters/create`)}
        className="gap-2"
      >
        <Plus className="h-4 w-4" />
        Add First Chapter
      </Button>
    </div>
  );
}

export function ChapterListSection({
  bookId,
  chapters,
}: {
  bookId: string | undefined;
  chapters: { id: string; number: number; title: string }[];
}) {
  const navigate = useNavigate();
  const sortedChapters = chapters.sort((a, b) => a.number - b.number);
  return (
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
            onClick={() => navigate(`/books/${bookId}/chapters/create`)}
            className="gap-2"
          >
            <Plus className="h-4 w-4" />
            Add Chapter
          </Button>
        </div>
      </CardHeader>
      <CardContent>
        {chapters.length === 0 ? (
          <NoChaptersPlaceholder bookId={bookId}/>
        ) : (
          <ul className="space-y-3">
            {sortedChapters.map((chapter) => (
              <li key={chapter.id}>
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
                      onClick={() => navigate(`/books/${bookId}/chapters/${chapter.id}`)}
                      className="gap-1"
                    >
                      <Eye className="h-3 w-3" />
                      View
                    </Button>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        )}
      </CardContent>
    </Card>
  );
}
