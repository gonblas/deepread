"use client"

import { useParams } from "react-router-dom"
import { ModernChapterEditor } from "./ModernChapterEditor"
import { ChapterSkeleton } from "./ChapterSkeleton"
import { ChapterNotFound } from "./ChapterNotFound"
import { useEffect } from "react"
import { useChapter } from "@/contexts/chapterContext"

export default function ChapterDetails() {
  const { bookId, chapterId } = useParams<{ bookId: string; chapterId: string }>()
  const { chapter, setChapter, loading, isSaving, fetchChapter, createChapter, updateChapter } = useChapter()
  const isNew = (chapterId === "create")

  useEffect(() => {
    if (bookId && chapterId && !isNew) {
      fetchChapter(bookId, chapterId)
    } else if (isNew) {
      setChapter({ id: "new", title: "", number: 1, summary: "" })
    }
  }, [isNew, bookId, chapterId, fetchChapter])

  if (loading) return <ChapterSkeleton />
  if (!chapter) return <ChapterNotFound />

  const handleSave = async (updatedChapter: typeof chapter): Promise<boolean> => {
    try {
      if (isNew && bookId) {
        await createChapter(bookId, updatedChapter)
      } else if (bookId && chapterId) {
        await updateChapter(bookId, chapterId, updatedChapter)
      }
      return true
    } catch (error) {
      return false
    }
  }

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
  )
}
