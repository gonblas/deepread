"use client"

import { QuizProvider, useQuiz } from "@/contexts/quizContext"
import { QuizCreator } from "@/components/quizzes/QuizCreator"
import { LoadingSpinner } from "@/components/ui/loading-spinner"
import { useNavigate, useParams } from "react-router-dom"

function CreateQuizContent() {
  const navigate = useNavigate()
  const { bookId, chapterId } = useParams()
  const { createQuiz, loading } = useQuiz()
  const { quiz } = useQuiz()
  const questions = quiz?.questions || []
  const handleSave = async () => {
    try {
      if (!bookId || !chapterId) {
        return
      }
      await createQuiz(bookId, chapterId, questions)
      navigate("/quizzes")
    } catch (error) {
      console.error("Failed to create quiz:", error)
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-center">
          <LoadingSpinner className="mx-auto mb-4" />
          <p className="text-muted-foreground">Creating quiz...</p>
        </div>
      </div>
    )
  }

  return (
    <QuizCreator onSave={handleSave} isEditing={false} />
  )
}

export default function CreateQuizPage() {
  return (
    <QuizProvider>
      <CreateQuizContent />
    </QuizProvider>
  )
}
