"use client";

import { useParams } from "react-router-dom";
import { ModernChapterEditor } from "./ModernChapterEditor";
import { ChapterSkeleton } from "./ChapterSkeleton";
import { ChapterNotFound } from "./ChapterNotFound";
import { useEffect } from "react";
import { useChapter } from "@/contexts/chapterContext";
import { useDataRefresh } from "@/contexts/dataRefreshContext";

export default function ChapterDetails() {
  const { bookId, chapterId } = useParams<{
    bookId: string;
    chapterId: string;
  }>();
  const {
    chapter,
    setChapter,
    loading,
    isSaving,
    fetchChapter,
    createChapter,
    updateChapter,
  } = useChapter();

  const { createResource, updateResource } = useDataRefresh();
  const isNew = chapterId === "create";

  useEffect(() => {
    if (bookId && chapterId && !isNew) {
      fetchChapter(bookId, chapterId);
    } else if (isNew) {
      setChapter({ id: "new", title: "", number: 1, summary: "" });
    }
  }, [isNew, bookId, chapterId, fetchChapter]);

  if (loading) return <ChapterSkeleton />;
  if (!chapter) return <ChapterNotFound />;

  const handleSave = async (
    updatedChapter: typeof chapter
  ): Promise<boolean> => {
    try {
      if (isNew && bookId) {
        await createResource(
          "chapter",
          () => createChapter(bookId, updatedChapter),
          `Chapter "${updatedChapter.title}" created successfully`,
          (result) => `/books/${bookId}/chapters/${result.id}`
        );
      } else if (bookId && chapterId) {
        await updateResource(
          "chapter",
          () => updateChapter(bookId, chapterId, updatedChapter),
          `Chapter "${updatedChapter.title}" updated successfully`
        );
      }
      return true;
    } catch (error) {
      return false;
    }
  };

  return (
    <ModernChapterEditor
      title={chapter.title}
      summary={chapter.summary}
      number={chapter.number}
      onTitleChange={(title) =>
        setChapter((prev) => prev && { ...prev, title })
      }
      onSummaryChange={(summary) =>
        setChapter((prev) => prev && { ...prev, summary })
      }
      onNumberChange={(number) =>
        setChapter((prev) => prev && { ...prev, number })
      }
      onSave={handleSave}
      isNew={isNew}
      isSaving={isSaving}
    />
  );
}
