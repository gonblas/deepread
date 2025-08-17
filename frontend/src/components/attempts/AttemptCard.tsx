"use client"

// import { formatDistanceToNow } from "date-fns"
import { Calendar, Clock, Target, ChevronRight, Trophy } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import type { QuizAttempt } from "@/contexts/attemptsContext"
import { Link } from "react-router-dom"

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

  // Extract chapter number from chapter title
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
          <Badge variant={getScoreBadgeVariant(percentage)} className="text-xs font-medium">
            <Trophy className="w-3 h-3 mr-1" />
            {percentage}%
          </Badge>
          {chapterNumber && (
            <Badge variant="outline" className="text-xs">
              Chapter {chapterNumber}
            </Badge>
          )}
        </div>

        <CardTitle
          className="text-lg leading-tight line-clamp-2 group-hover:text-primary transition-colors"
          title={attempt.quiz?.title || `Quiz ${attempt.quiz_id.slice(0, 8)}`}
        >
          {attempt.quiz?.title || `Quiz ${attempt.quiz_id.slice(0, 8)}`}
        </CardTitle>

        <div className="flex items-center gap-4 text-sm text-muted-foreground">
          <div className="flex items-center gap-1">
            <Calendar className="w-3 h-3 flex-shrink-0" />
            <span title={submittedDate.toLocaleString()}>
              {/* {formatDistanceToNow(submittedDate, { addSuffix: true })} */}
              nashei
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
              <Target className="size-4 mr-1" />
              View Results
              <ChevronRight className="size-4 ml-1" />
            </Link>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
