"use client";

import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { formatTime } from "@/lib/formatTime";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorMessage } from "@/components/ui/error-message";
import { BackButton } from "@/components/ui/back-button";
import {
  CheckCircle,
  XCircle,
  Clock,
  Target,
  TrendingUp,
  RotateCcw,
  ArrowLeft,
} from "lucide-react";
import { useNotification } from "@/contexts/notificationContext";
import Cookies from "js-cookie";
import { useAuth } from "@/contexts/authContext";
import { QuestionDisplay } from "@/components/QuestionDisplay";
import { Badge } from "@/components/ui/badge";

interface QuizAttemptResult {
  id: string;
  quiz_id: string;
  answers: Array<{ correct: boolean }>;
  correctCount: number;
  startedAt: string;
  submittedAt: string;
  quiz: {
    id: string;
    chapter: {
      id: string;
      title: string;
      number: number;
      summary: string;
    };
    questions: Array<{
      id: string;
      type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN";
      prompt: string;
      explanation: string;
      options?: Array<{
        id: string;
        text: string;
        isCorrect: boolean;
      }>;
      isAnswerTrue?: boolean;
      expectedAnswer?: string;
    }>;
  };
  userAnswers: Array<{
    questionId: string;
    type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN";
    optionIds?: string[];
    answer?: boolean;
    answerText?: string;
  }>;
}

export default function QuizResultsPage() {
  const params = useParams();
  const navigate = useNavigate();
  const attemptId = params.attemptId as string;
  const { showError } = useNotification();
  const { logout } = useAuth();

  // State
  const [result, setResult] = useState<QuizAttemptResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchResults = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(
        `http://localhost:8080/api/attempts/${attemptId}`,
        {
          headers: {
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          logout();
          return;
        }
        if (response.status === 404) {
          throw new Error("Attempt not found");
        }
      }

      const raw = await response.json();

      const mapped: QuizAttemptResult = {
        id: raw.id,
        quiz_id: raw.quiz_id,
        answers: raw.answers.map((a: any) => ({
          correct: a.isCorrect,
        })),
        correctCount: raw.correctCount,
        startedAt: raw.startedAt,
        submittedAt: raw.submittedAt,
        quiz: {
          id: raw.quiz_id,
          chapter: {
            id: "",
            title: "",
            number: 0,
            summary: "",
          },
          questions: raw.answers.map((a: any) => ({
            id: a.question.id,
            type: a.question.type,
            prompt: a.question.prompt,
            explanation: a.question.explanation,
            options: a.question.options
              ? a.question.options.map((opt: any) => ({
                  id: opt.id,
                  text: opt.text,
                  isCorrect: opt.isCorrect,
                }))
              : undefined,
            isAnswerTrue: a.question.isAnswerTrue,
            expectedAnswer: a.question.expectedAnswer,
          })),
        },
        userAnswers: raw.answers.map((a: any) => ({
          questionId: a.question.id,
          type: a.type,
          optionIds: a.optionsSelected || [],
          answer: a.answer,
          answerText: a.answerText,
        })),
      };

      setResult(mapped);
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to fetch results";
      setError(errorMessage);
      showError("Error", errorMessage);
    } finally {
      setLoading(false);
    }
  }, [attemptId, showError]);

  useEffect(() => {
    if (attemptId) {
      fetchResults();
    }
  }, [attemptId, fetchResults]);

  // Calculate metrics
  const totalQuestions = result?.quiz.questions.length || 0;
  const correctAnswers = result?.correctCount || 0;
  const incorrectAnswers = totalQuestions - correctAnswers;
  const percentage =
    totalQuestions > 0
      ? Math.round((correctAnswers / totalQuestions) * 100)
      : 0;

  const durationInSeconds = result
    ? (new Date(result.submittedAt).getTime() -
        new Date(result.startedAt).getTime()) /
      1000
    : 0;

  const getPerformanceMessage = (percentage: number) => {
    if (percentage >= 90)
      return {
        message: "Excellent work! Outstanding performance!",
        color: "text-green-600",
      };
    if (percentage >= 80)
      return {
        message: "Great job! Very good understanding!",
        color: "text-green-600",
      };
    if (percentage >= 70)
      return {
        message: "Good work! Room for improvement.",
        color: "text-yellow-600",
      };
    if (percentage >= 60)
      return {
        message: "Fair performance. Consider reviewing the material.",
        color: "text-orange-600",
      };
    return {
      message: "Needs improvement. Please review the chapter.",
      color: "text-red-600",
    };
  };

  const performanceMessage = getPerformanceMessage(percentage);

  if (loading) {
    return (
      <div className="container mx-auto py-8">
        <div className="flex items-center justify-center min-h-[400px]">
          <LoadingSpinner size="lg" />
        </div>
      </div>
    );
  }

  if (error || !result) {
    return (
      <div className="container mx-auto py-8">
        <BackButton href="/quizzes" />
        <ErrorMessage message={error || "Results not found"} className="mt-4" />
      </div>
    );
  }

  return (
    <div className="container mx-auto py-8 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <BackButton href="/quizzes" />
        <div className="flex-1">
          <h1 className="text-3xl font-bold">Quiz Results</h1>
          <p className="text-muted-foreground">
            Chapter {result.quiz.chapter.number}: {result.quiz.chapter.title}
          </p>
        </div>
      </div>

      {/* Score Overview */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card>
          <CardContent className="p-6 text-center">
            <div className="flex items-center justify-center mb-2">
              <Target className="h-8 w-8 text-blue-500" />
            </div>
            <div className="text-3xl font-bold mb-1">{percentage}%</div>
            <div className="text-sm text-muted-foreground">Final Score</div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6 text-center">
            <div className="flex items-center justify-center mb-2">
              <CheckCircle className="h-8 w-8 text-green-500" />
            </div>
            <div className="text-3xl font-bold mb-1 text-green-600">
              {correctAnswers}
            </div>
            <div className="text-sm text-muted-foreground">Correct</div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6 text-center">
            <div className="flex items-center justify-center mb-2">
              <XCircle className="h-8 w-8 text-red-500" />
            </div>
            <div className="text-3xl font-bold mb-1 text-red-600">
              {incorrectAnswers}
            </div>
            <div className="text-sm text-muted-foreground">Incorrect</div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6 text-center">
            <div className="flex items-center justify-center mb-2">
              <Clock className="h-8 w-8 text-orange-500" />
            </div>
            <div className="text-3xl font-bold mb-1">
              {formatTime(durationInSeconds)}
            </div>
            <div className="text-sm text-muted-foreground">Time</div>
          </CardContent>
        </Card>
      </div>

      {/* Performance Message */}

      <div className="p-6 text-center">
        <div className="flex items-center justify-center gap-2 mb-2 p-6 text-center">
          <TrendingUp className={`h-6 w-6 ${performanceMessage.color}`} />
          <span className={`text-xl font-semibold ${performanceMessage.color}`}>
            {performanceMessage.message}
          </span>
        </div>
        <p className="text-muted-foreground">
          You answered {correctAnswers} out of {totalQuestions} questions
          correctly.
        </p>
      </div>

      {/* Question Breakdown */}
      <div className="space-y-6">
        <div className="flex items-center gap-3">
          <h2 className="text-3xl font-bold text-foreground">
            Question Breakdown
          </h2>
        </div>

        <div className="space-y-6">
          {result.quiz.questions.map((question, index) => {
            const userAnswer = result.userAnswers.find(
              (ua) => ua.questionId === question.id
            );
            const isCorrect = result.answers[index]?.correct;

            return (
              <QuestionDisplay
                key={question.id}
                question={question}
                index={index}
                mode="results"
                userAnswer={userAnswer}
                isCorrect={isCorrect}
              />
            );
          })}
        </div>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Attempt Details</CardTitle>
        </CardHeader>
        <CardContent className="space-y-2">
          <div className="flex justify-between">
            <span className="text-muted-foreground">Started:</span>
            <span>{new Date(result.startedAt).toLocaleString()}</span>
          </div>
          <div className="flex justify-between">
            <span className="text-muted-foreground">Completed:</span>
            <span>{new Date(result.submittedAt).toLocaleString()}</span>
          </div>
          <div className="flex justify-between">
            <span className="text-muted-foreground">Attempt ID:</span>
            <span className="font-mono text-sm">{result.id}</span>
          </div>
        </CardContent>
      </Card>

      {/* Actions */}
      <div className="flex gap-4 justify-center">
        <Button variant="outline" onClick={() => navigate("/quizzes")}>
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Quizzes
        </Button>
        <Button onClick={() => navigate(`/quizzes/${result.quiz_id}/take`)}>
          <RotateCcw className="mr-2 h-4 w-4" />
          Retake Quiz
        </Button>
      </div>
    </div>
  );
}
