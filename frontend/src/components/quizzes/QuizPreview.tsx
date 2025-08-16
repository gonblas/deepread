"use client"

import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { FileText, Eye } from "lucide-react"
import { QuestionDisplay } from "@/components/QuestionDisplay"
import type { Question } from "@/contexts/quizContext"

interface QuizPreviewProps {
  questions: Question[]
  showActions?: boolean
}

export function QuizPreview({ questions, showActions = true }: QuizPreviewProps) {
  if (questions.length === 0) {
    return (
      <Card>
        <CardContent className="p-8 text-center">
          <FileText className="h-12 w-12 text-muted-foreground mx-auto mb-4" />
          <h3 className="text-lg font-semibold mb-2">No Questions Yet</h3>
          <p className="text-muted-foreground">Add some questions to see the quiz preview.</p>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Eye className="h-6 w-6" />
          <h2 className="text-2xl font-bold">Quiz Preview</h2>
        </div>
        <Badge variant="outline">{questions.length} Questions</Badge>
      </div>

      <div className="space-y-6">
        {questions.map((question, index) => (
          <QuestionDisplay key={question.id} question={question} index={index} mode="preview" />
        ))}
      </div>

      {showActions && (
        <div className="flex justify-center pt-4">
          <Button size="lg">Export Quiz</Button>
        </div>
      )}
    </div>
  )
}
