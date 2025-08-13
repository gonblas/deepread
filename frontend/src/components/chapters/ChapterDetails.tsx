import { useParams } from "react-router-dom";
import { ModernChapterEditor } from "./ModernChapterEditor";
import { ChapterSkeleton } from "./ChapterSkeleton";
import { ChapterNotFound } from "./ChapterNotFound";
import { useEffect } from "react";
import { useChapter } from "@/contexts/chapterContext";

export default function ChapterDetails({ isNew }: { isNew: boolean }) {
  const { bookId, chapterId } = useParams<{ bookId: string , chapterId: string}>();
  const { chapter, setChapter, loading, isSaving, fetchChapter, createChapter, updateChapter } = useChapter();

  useEffect(() => {
    if (bookId && chapterId) {
      fetchChapter(bookId, chapterId);
    } else if (isNew) {
      setChapter({ id: "new", title: "", number: 1, summary: "" });
    }
  }, [isNew, bookId, chapterId, fetchChapter]);

  if (loading) return <ChapterSkeleton />;
  if (!chapter) return <ChapterNotFound />;

  const handleSave = async () => {
    if (isNew && bookId) {
      await createChapter(bookId, chapter);
    } else {
      if(bookId){
        await updateChapter(bookId, chapter);
      }
    }
  };

  return (
    <ModernChapterEditor
      title={chapter.title}
      summary={chapter.summary}
      number={chapter.number}
      onTitleChange={(title) => setChapter((prev) => prev && { ...prev, title })}
      onSummaryChange={(summary) => setChapter((prev) => prev && { ...prev, summary })}
      onNumberChange={(number) => setChapter((prev) => prev && { ...prev, number })}
      onSave={handleSave}
      isNew={isNew}
      onDelete={() => {}}
      isSaving={isSaving}
    />
  );
}
