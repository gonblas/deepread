"use client";

import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group";
import { Label } from "@/components/ui/label";
import { Textarea } from "@/components/ui/textarea";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorMessage } from "@/components/ui/error-message";
import { BackButton } from "@/components/ui/back-button";
import { BookOpen, CheckCircle, Circle, Send } from "lucide-react";
import { useNotification } from "@/contexts/notificationContext";
import Cookies from "js-cookie";
import {
  type Quiz,
  type MultipleChoiceQuestion,
  type TrueFalseQuestion,
  type OpenQuestion,
  useQuizContext,
  useQuiz,
  QuizProvider,
} from "@/contexts/quizContext";
import { SectionHeader } from "@/components/SectionHeader";
import { ClockCard } from "@/components/ClockCard";
import { useAuth } from "@/contexts/authContext";

interface QuizAnswer {
  type: "MULTIPLE_CHOICE" | "TRUE_FALSE" | "OPEN";
  questionId: string;
  optionIds?: string[];
  answer?: boolean;
  answerText?: string;
}

interface QuizAttempt {
  startedAt: string;
  answers: QuizAnswer[];
}

function TakeQuizComponent() {
  const { quizId } = useParams<{ quizId: string }>();
  const navigate = useNavigate();
  const { showError, showSuccess } = useNotification();
  const { fetchQuiz, quiz, loading, error } = useQuiz();
  const { logout } = useAuth();

  const [submitting, setSubmitting] = useState(false);
  const [answers, setAnswers] = useState<Record<string, QuizAnswer>>({});
  const [startTime] = useState<Date>(new Date());
  const [elapsedTime, setElapsedTime] = useState<number>(0);
  const totalQuestions = quiz?.questions.length || 0;
  const answeredCount = Object.keys(answers).length;

  useEffect(() => {
    const interval = setInterval(() => {
      setElapsedTime(Math.floor((Date.now() - startTime.getTime()) / 1000));
    }, 1000);

    return () => clearInterval(interval);
  }, [startTime]);

  useEffect(() => {
    if (quizId) fetchQuiz(quizId);
  }, [quizId]);

  const handleAnswerChange = useCallback(
    (questionId: string, answer: QuizAnswer) => {
      setAnswers((prev) => ({
        ...prev,
        [questionId]: answer,
      }));
    },
    []
  );

  // Check if all questions are answered
  const allQuestionsAnswered =
    quiz?.questions.every((question) => answers[question.id] !== undefined) ??
    false;

  // Submit quiz
  const handleSubmit = useCallback(async () => {
    if (!quiz || !allQuestionsAnswered) return;
    setSubmitting(true);

    try {
      const quizAttempt: QuizAttempt = {
        startedAt: startTime.toISOString(),
        answers: Object.values(answers),
      };

      const response = await fetch(
        `http://localhost:8080/api/quizzes/${quizId}/attempts`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
          body: JSON.stringify(quizAttempt),
        }
      );

      if (!response.ok) {
        if(response.status === 401) {
          logout();
          return;
        }
        throw new Error("Failed to submit quiz");
      }

      const result = await response.json();

      showSuccess(
        "Quiz Submitted",
        "Your answers have been submitted successfully!"
      );

      navigate(`/quizzes/attempts/${result.id}`);
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to submit quiz";
      showError("Submission Error", errorMessage);
    } finally {
      setSubmitting(false);
    }
  }, [
    quiz,
    allQuestionsAnswered,
    startTime,
    answers,
    quizId,
    navigate,
    showError,
    showSuccess,
  ]);

  if (loading) {
    return (
      <div className="container mx-auto py-8">
        <div className="flex items-center justify-center min-h-[400px]">
          <LoadingSpinner size="lg" />
        </div>
      </div>
    );
  }

  if (error || !quiz) {
    return (
      <div className="container mx-auto py-8">
        <BackButton href="/quizzes" />
        <ErrorMessage message={error || "Quiz not found"} className="mt-4" />
      </div>
    );
  }

  return (
    <div className="container mx-auto py-8 space-y-6">
      {/* Header */}
      <div className="flex items-center gap-4">
        <SectionHeader
          title="Take Quiz"
          description="Answer all questions to complete the quiz"
          loading={false}
          error={null}
        >
          <ClockCard time={elapsedTime} />
        </SectionHeader>
      </div>

      {quiz.chapter && (
        <Card>
          <CardHeader>
            <div className="flex items-center gap-2">
              <BookOpen className="h-5 w-5" />
              <CardTitle>
                Chapter {quiz.chapter.number}: {quiz.chapter.title}
              </CardTitle>
            </div>
            <CardDescription>{quiz.chapter.summary}</CardDescription>
          </CardHeader>
        </Card>
      )}

      <div className="space-y-6">
        {quiz.questions.map((question, index) => (
          <Card key={question.id}>
            <CardHeader>
              <div className="flex items-start gap-3">
                <Badge variant="outline" className="mt-1">
                  {index + 1}
                </Badge>
                <CardTitle className="text-lg">{question.prompt}</CardTitle>
              </div>
            </CardHeader>

            <CardContent className="space-y-4">
              {/* Multiple Choice */}
              {question.type === "MULTIPLE_CHOICE" && (
                <RadioGroup
                  value={answers[question.id]?.optionIds?.[0] || ""}
                  onValueChange={(value) => {
                    handleAnswerChange(question.id, {
                      type: "MULTIPLE_CHOICE",
                      questionId: question.id,
                      optionIds: [value],
                    });
                  }}
                >
                  {(question as MultipleChoiceQuestion).options.map(
                    (option) => (
                      <div
                        key={option.id}
                        className="flex items-center space-x-2 p-3 rounded-lg border hover:bg-muted/50"
                      >
                        <RadioGroupItem value={option.id} id={option.id} />
                        <Label
                          htmlFor={option.id}
                          className="flex-1 cursor-pointer"
                        >
                          {option.text}
                        </Label>
                      </div>
                    )
                  )}
                </RadioGroup>
              )}

              {/* True/False */}
              {question.type === "TRUE_FALSE" && (
                <RadioGroup
                  value={answers[question.id]?.answer?.toString() || ""}
                  onValueChange={(value) => {
                    handleAnswerChange(question.id, {
                      type: "TRUE_FALSE",
                      questionId: question.id,
                      answer: value === "true",
                    });
                  }}
                >
                  <div className="flex items-center space-x-2 p-3 rounded-lg border hover:bg-muted/50">
                    <RadioGroupItem value="true" id={`${question.id}-true`} />
                    <Label
                      htmlFor={`${question.id}-true`}
                      className="flex-1 cursor-pointer"
                    >
                      True
                    </Label>
                  </div>
                  <div className="flex items-center space-x-2 p-3 rounded-lg border hover:bg-muted/50">
                    <RadioGroupItem value="false" id={`${question.id}-false`} />
                    <Label
                      htmlFor={`${question.id}-false`}
                      className="flex-1 cursor-pointer"
                    >
                      False
                    </Label>
                  </div>
                </RadioGroup>
              )}

              {/* Open Ended */}
              {question.type === "OPEN" && (
                <Textarea
                  placeholder="Type your answer here..."
                  value={answers[question.id]?.answerText || ""}
                  onChange={(e) => {
                    handleAnswerChange(question.id, {
                      type: "OPEN",
                      questionId: question.id,
                      answerText: e.target.value,
                    });
                  }}
                  className="min-h-[100px]"
                />
              )}
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="fixed bottom-0 left-0 right-0 z-50 bg-background border-t md:left-[var(--sidebar-width)]">
        <div className="px-6 py-2">
          <div className="flex items-center gap-4">
            {/* Status and counter */}
            <div className="flex items-center gap-2 text-sm min-w-0">
              {allQuestionsAnswered ? (
                <>
                  <CheckCircle className="h-4 w-4 text-green-500 shrink-0" />
                  <span className="font-medium text-green-600">Ready!</span>
                </>
              ) : (
                <>
                  <Circle className="h-4 w-4 text-muted-foreground shrink-0" />
                  <span className="text-muted-foreground">
                    {totalQuestions - answeredCount} left
                  </span>
                </>
              )}
              <span className="text-muted-foreground">•</span>
              <span className="text-muted-foreground font-mono">
                {answeredCount}/{totalQuestions}
              </span>
            </div>

            {/* Progress bar */}
            <div className="flex-1 bg-muted rounded-full h-1.5">
              <div
                className="bg-primary h-1.5 rounded-full transition-all duration-500"
                style={{
                  width: `${totalQuestions > 0 ? (answeredCount / totalQuestions) * 100 : 0}%`,
                }}
              />
            </div>

            {/* Submit button */}
            <Button
              size="sm"
              onClick={handleSubmit}
              disabled={!allQuestionsAnswered || submitting}
              className="shrink-0"
            >
              {submitting ? (
                <>
                  <LoadingSpinner size="sm" className="mr-1" />
                  Submitting...
                </>
              ) : (
                <>
                  <Send className="mr-1 h-3 w-3" />
                  Submit
                </>
              )}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function TakeQuizPage() {
  return (
    <QuizProvider>
      <TakeQuizComponent />
    </QuizProvider>
  );
}
