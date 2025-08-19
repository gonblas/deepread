"use client"

import { Calendar, Clock, ChevronRight, Trophy } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import type { QuizAttempt } from "@/contexts/attemptsContext"
import { Link } from "react-router-dom"
import { formatDistanceToNow } from "date-fns"
import { CardBadge } from "../card/CardBadge"

interface AttemptCardProps {
  attempt: QuizAttempt
}

export function AttemptCard({ attempt }: AttemptCardProps) {
  const totalQuestions = attempt.answers.length
  const percentage = totalQuestions > 0 ? Math.round((attempt.correctCount / totalQuestions) * 100) : 0
  const submittedDate = new Date(attempt.submittedAt)

  const getScoreBadgeVariant = (score: number): "default" | "secondary" | "destructive" => {
    if (score >= 80) return "default"
    if (score >= 60) return "secondary"
    return "destructive"
  }

  const getChapterNumber = (chapterTitle?: string) => {
    if (!chapterTitle) return null
    const match = chapterTitle.match(/Chapter (\d+)/)
    return match ? match[1] : null
  }

  const chapterNumber = getChapterNumber(attempt.quiz?.chapter?.title)

  return (
    <Card className="group hover:shadow-lg transition-all duration-200 border-0 shadow-md h-full flex flex-col">
      <CardHeader className="pb-3 flex-shrink-0">
        <div className="flex items-start justify-between gap-2 mb-3">
          <CardBadge
              icon={Trophy}
              text={`${percentage}%`}
              variant={getScoreBadgeVariant(percentage)}
            />
          {chapterNumber && (
            <Badge variant="outline" className="text-xs">
              Chapter {chapterNumber}
            </Badge>
          )}
        </div>

        <div className="flex flex-col items-start gap-4 text-sm text-muted-foreground">
          <div className="flex items-center gap-1">
            <Calendar className="w-3 h-3 flex-shrink-0" />
            <span title={submittedDate.toLocaleString()}>
              {formatDistanceToNow(submittedDate, { addSuffix: true })}
            </span>
          </div>
          <div className="flex items-center gap-1">
            <Clock className="w-3 h-3 flex-shrink-0" />
            <span>{submittedDate.toLocaleDateString()}</span>
          </div>
        </div>
      </CardHeader>

      <CardContent className="pt-0 flex flex-col flex-grow">
        <div className="flex-grow">
          <div className="flex items-center justify-between text-sm">
            <span className="text-muted-foreground">Score</span>
            <span className="font-medium">
              {attempt.correctCount}/{totalQuestions} questions
            </span>
          </div>
        </div>

        <div className="mt-4 pt-4 border-t flex-shrink-0">
          <Button asChild className="w-full text-xs">
            <Link to={`/quizzes/attempts/${attempt.id}`}>
              View Results
              <ChevronRight className="size-4 ml-1" />
            </Link>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
