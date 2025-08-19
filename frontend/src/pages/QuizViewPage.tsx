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
import {
  Play,
  Edit,
  Clock,
  Hash,
  BookOpen,
  FileQuestion,
  Download,
  MoreHorizontal,
} from "lucide-react";
import { RecentAttemptsTable } from "@/components/statistics/RecentAttemptsTable";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { DeleteElementDialog } from "@/components/DeleteElementDialog";
import {
  RecentAttemptsProvider,
  useRecentAttempts,
} from "@/contexts/recentAttemptsContext";

interface StatCardProps {
  icon: React.ReactNode;
  title: string;
  value: string | number;
  iconBgColor: string;
}

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
    quizAttempts,
    fetchQuizRecentAttempts,
    loading: loadingRecentAttempts,
    error: errorRecentAttempts,
  } = useRecentAttempts();
  const {
    quizStats,
    loading: statsLoading,
    error: statsError,
    fetchQuizStats,
  } = useStatistics();

  useEffect(() => {
    fetchQuiz(quizId);
    fetchQuizRecentAttempts(quizId);
  }, [quizId]);

  const chapterId = quiz?.chapter?.id;
  useEffect(() => {
    if (chapterId) fetchQuizStats(chapterId);
  }, [quiz]);

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
          {quizId && (
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button
                  variant="outline"
                  size="sm"
                  className="bg-background/50"
                >
                  <MoreHorizontal className="h-4 w-4" />
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end" className="w-54">
                <DropdownMenuLabel>Quiz Actions</DropdownMenuLabel>
                <DropdownMenuItem
                  // onClick={onExport}
                  className="w-full gap-2 flex justify-center"
                >
                  <Download className="size-4" />
                  Export as Markdown
                </DropdownMenuItem>
                <DropdownMenuSeparator />
                <DeleteElementDialog
                  deleteURL={`http://localhost:8080/api/quizzes/${quizId}`}
                  redirectURL={`/quizzes/`}
                  resourceType="quiz"
                  resourceName={quiz.chapter?.title || "Unknown"}
                  fullWidth={true}
                />
              </DropdownMenuContent>
            </DropdownMenu>
          )}
        </div>
      </SectionHeader>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {statsConfig.map((stat, index) => (
          <StatCard key={index} {...stat} />
        ))}

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
          <RecentAttemptsTable
            recentAttempts={quizAttempts}
            loading={loadingRecentAttempts}
            error={errorRecentAttempts}
          />
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
        <RecentAttemptsProvider>
          <QuizViewContent quizId={quizId} />
        </RecentAttemptsProvider>
      </StatisticsProvider>
    </QuizProvider>
  );
}
