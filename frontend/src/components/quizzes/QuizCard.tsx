"use client"

import { useState } from "react"
import { Link } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { BookOpen, HelpCircle, Edit, Brain } from "lucide-react"

interface Chapter {
  id: string
  title: string
  number: number
  summary: string
}

interface Quiz {
  id: string
  chapter: Chapter
  questions: Array<{
    id: string
    type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN"
    prompt: string
    explanation: string
  }>
}

interface QuizCardProps {
  quiz: Quiz
  onDelete: (quizId: string) => void
}

export function QuizCard({ quiz, onDelete }: QuizCardProps) {
  const [showDeleteDialog, setShowDeleteDialog] = useState(false)

  const handleDelete = () => {
    onDelete(quiz.id)
    setShowDeleteDialog(false)
  }

  const handleOpenQuiz = () => {
    window.location.href = `/quizzes/${quiz.id}`
  }

  return (
    <>
      <Card className="group hover:shadow-lg transition-all duration-200 border-0 shadow-md h-full flex flex-col">
        <CardHeader className="pb-3 flex-shrink-0">
          <div className="flex items-start justify-between gap-2 mb-3">
            <Badge variant="secondary" className="text-xs font-medium bg-blue-100 text-blue-800">
              <BookOpen className="w-3 h-3 mr-1" />
              Chapter {quiz.chapter.number}
            </Badge>
          </div>

          <CardTitle
            className="text-lg leading-tight line-clamp-1 group-hover:text-primary transition-colors"
            title={quiz.chapter.title}
          >
            {quiz.chapter.title}
          </CardTitle>

          <div className="flex items-center gap-1 text-sm text-muted-foreground">
            <HelpCircle className="w-3 h-3 flex-shrink-0" />
            <span className="line-clamp-1">
              {quiz.questions.length} {quiz.questions.length === 1 ? "question" : "questions"}
            </span>
          </div>
        </CardHeader>

        <CardContent className="pt-0 flex flex-col flex-grow">
          <div className="flex-grow">
            <CardDescription className="text-sm leading-relaxed line-clamp-4" title={quiz.chapter.summary}>
              {quiz.chapter.summary || "No summary available for this chapter."}
            </CardDescription>
          </div>

          <div className="mt-4 pt-4 border-t flex-shrink-0">
            <div className="flex gap-2">
              <Button className="flex-1 text-xs" onClick={handleOpenQuiz}>
                <Brain className="size-4 mr-1" />
                Open Quiz
              </Button>
              <Button variant="outline" className="px-3 bg-transparent" asChild>
                <Link to={`/quizzes/${quiz.id}/edit`}>
                  <Edit className="size-4" />
                  Edit
                </Link>
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <AlertDialog open={showDeleteDialog} onOpenChange={setShowDeleteDialog}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Quiz</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete this quiz for "{quiz.chapter.title}"? This action cannot be undone and
              will permanently remove all {quiz.questions.length} questions.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDelete}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              Delete Quiz
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </>
  )
}
