"use client";

import type React from "react";

import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { SectionHeader } from "@/components/SectionHeader";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { ErrorMessage } from "@/components/ui/error-message";
import { EmptyState } from "@/components/ui/empty-state";
import { SectionCards } from "@/components/statistics/SectionCards";
import { BarChartInteractive } from "@/components/statistics/BarChartInteractive";
import { BackButton } from "@/components/ui/back-button";
import { QuizProvider, useQuiz } from "@/contexts/quizContext";
import {
  StatisticsProvider,
  useStatistics,
} from "@/contexts/statisticsContext";
import { Play, Edit, Clock, Hash, BookOpen, FileQuestion } from "lucide-react";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";

interface StatCardProps {
  icon: React.ReactNode;
  title: string;
  value: string | number;
  iconBgColor: string;
}

const recentAttempts = [
  {
    id: "1",
    bookTitle: "One Hundred Years of Solitude",
    chapterNumber: 1,
    score: 85,
    date: "2025-08-09T09:00:00"
  },
  {
    id: "2",
    bookTitle: "Don Quixote",
    chapterNumber: 2,
    score: 72,
    date: "2025-08-09T07:00:00"
  },
  {
    id: "3",
    bookTitle: "Hopscotch",
    chapterNumber: 3,
    score: 45,
    date: "2025-08-08T10:00:00"
  },
  {
    id: "4",
    bookTitle: "The House of the Spirits",
    chapterNumber: 4,
    score: 88,
    date: "2025-08-08T09:00:00"
  },
  {
    id: "5",
    bookTitle: "Love in the Time of Cholera",
    chapterNumber: 2,
    score: 91,
    date: "2025-08-07T10:00:00"
  },
]

function StatCard({ icon, title, value, iconBgColor }: StatCardProps) {
  return (
    <Card>
      <CardContent className="p-4">
        <div className="flex items-center gap-3">
          <div className={`p-2 rounded-lg ${iconBgColor}`}>{icon}</div>
          <div>
            <p className="text-sm text-muted-foreground">{title}</p>
            <p className="text-2xl font-bold">{value}</p>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

function QuizViewContent({ quizId }: { quizId: string }) {
  const navigate = useNavigate();
  const { quiz, loading: quizLoading, error: quizError, fetchQuiz } = useQuiz();
  const {
    quizStats,
    quizLoading: statsLoading,
    quizError: statsError,
    fetchQuizStats,
  } = useStatistics();

  useEffect(() => {
    fetchQuiz(quizId);
  }, [quizId]);

  const chapterId = quiz?.chapter?.id;
  useEffect(() => {
    if(chapterId)
      fetchQuizStats(chapterId);
  }, [chapterId, quizStats]);

  const handleTakeQuiz = () => {
    navigate(`/quizzes/${quizId}/take`);
  };

  const handleEditQuiz = () => {
    navigate(`/quizzes/${quizId}/edit`);
  };

  if (quizLoading) {
    return (
      <div className="container mx-auto py-8">
        <BackButton href="/quizzes" label="Back to Quizzes" />
        <LoadingSpinner size="lg" />
      </div>
    );
  }

  if (quizError) {
    return (
      <div className="container mx-auto py-8">
        <BackButton href="/quizzes" label="Back to Quizzes" />
        <ErrorMessage message={quizError} />
      </div>
    );
  }

  if (!quiz) {
    return (
      <div className="container mx-auto py-8">
        <BackButton href="/quizzes" label="Back to Quizzes" />
        <EmptyState
          title="Quiz not found"
          description="The quiz you're looking for doesn't exist or has been deleted."
          icon={<FileQuestion className="h-12 w-12" />}
        />
      </div>
    );
  }

  const getQuestionTypeCounts = () => {
    const counts = {
      MULTIPLE_CHOICE: quiz.questions.filter(
        (q) => q.type === "MULTIPLE_CHOICE"
      ).length,
      TRUE_FALSE: quiz.questions.filter((q) => q.type === "TRUE_FALSE").length,
      OPEN: quiz.questions.filter((q) => q.type === "OPEN").length,
    };
    return counts;
  };

  const questionTypeCounts = getQuestionTypeCounts();
  const estimatedDuration = Math.ceil(quiz.questions.length * 1.5);

  const statsConfig = [
    {
      icon: <Hash className="h-4 w-4 text-blue-600 dark:text-blue-400" />,
      title: "Total Questions",
      value: quiz.questions.length,
      iconBgColor: "bg-blue-100 dark:bg-blue-900",
    },
    {
      icon: <Clock className="h-4 w-4 text-green-600 dark:text-green-400" />,
      title: "Estimated Duration",
      value: `${estimatedDuration} min`,
      iconBgColor: "bg-green-100 dark:bg-green-900",
    },
    ...(quiz.chapter
      ? [
          {
            icon: (
              <BookOpen className="h-4 w-4 text-purple-600 dark:text-purple-400" />
            ),
            title: "Chapter",
            value: `#${quiz.chapter.number}`,
            iconBgColor: "bg-purple-100 dark:bg-purple-900",
          },
        ]
      : []),
  ];

  // Question type badges configuration
  const questionTypeBadges = [
    {
      type: "MULTIPLE_CHOICE",
      label: "MC",
      count: questionTypeCounts.MULTIPLE_CHOICE,
    },
    { type: "TRUE_FALSE", label: "T/F", count: questionTypeCounts.TRUE_FALSE },
    { type: "OPEN", label: "Open", count: questionTypeCounts.OPEN },
  ].filter((badge) => badge.count > 0);

  return (
    <div className="container space-y-8">
      <SectionHeader
        title={`Quiz for ${quiz.chapter?.title || "Unknown Chapter"}`}
        loading={false}
        error={false}
      >
        <div className="flex items-center gap-2 ml-auto">
          <Button
            variant="outline"
            size="sm"
            onClick={handleEditQuiz}
            className="gap-2 bg-transparent"
          >
            <Edit className="w-4 h-4" />
            Edit
          </Button>
          <Button size="sm" onClick={handleTakeQuiz} className="gap-2">
            <Play className="w-4 h-4" />
            Take Quiz
          </Button>
        </div>
      </SectionHeader>

      {/* Quiz Basic Statistics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {statsConfig.map((stat, index) => (
          <StatCard key={index} {...stat} />
        ))}

        {/* Question Types Card */}
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="p-2 bg-orange-100 dark:bg-orange-900 rounded-lg">
                <FileQuestion className="h-4 w-4 text-orange-600 dark:text-orange-400" />
              </div>
              <div className="space-y-2">
                <p className="text-sm text-muted-foreground">Question Types</p>
                <div className="flex flex-wrap gap-1">
                  {questionTypeBadges.map(({ type, label, count }) => (
                    <Badge key={type} variant="secondary" className="text-xs">
                      {label}: {count}
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {statsLoading && <LoadingSpinner size="md" />}

      {quizStats && !statsLoading && !statsError && (
        <div className="space-y-6">
          <h2 className="text-2xl font-bold">Quiz Performance Statistics</h2>
          <SectionCards stats={quizStats.stats} />
          <BarChartInteractive chartData={quizStats.dailyStatsTimeline} />
          <RecentAttemptsTable recentAttempts={recentAttempts} />
        </div>
      )}
    </div>
  );
}

export default function QuizViewPage() {
  const { quizId } = useParams<{ quizId: string }>();
  if (!quizId) {
    return <ErrorMessage message="Quiz ID is required" />;
  }
  return (
    <QuizProvider>
      <StatisticsProvider>
        <QuizViewContent quizId={quizId} />
      </StatisticsProvider>
    </QuizProvider>
  );
}
