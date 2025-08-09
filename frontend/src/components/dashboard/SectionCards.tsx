import { Badge } from "@/components/ui/badge"
import {
  Card,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { BarChart3, Clock, AlertTriangle, Award } from "lucide-react"

type Stats = {
  totalAttempts: number;
  totalQuizzesAttempted: number;
  averageScore: number;
  bestScore: number;
  worstScore: number;
  averageTimeSeconds: number;
  dailyStatsTimeline: {
    date: string;
    attempts: number;
    averageScore: number;
  }[];
};

export function SectionCards({ stats }: { stats: Stats }) {

  const cardConfig = [
    {
      key: "attempts",
      description: "Attempts",
      icon: <BarChart3 className="w-6 h-6 text-primary" />,
      badge: (stats: Stats) => (
        <span>
          <span className="font-bold">{stats.totalAttempts}</span> attempts
          <Badge className="ml-2" variant="secondary">
            {stats.totalQuizzesAttempted} quizzes
          </Badge>
        </span>
      ),
      value: (stats: Stats) => stats.totalAttempts,
      footerMain: `Across ${stats.totalQuizzesAttempted} quizzes`,
    },
    {
      key: "averageScore",
      description: "Average Score",
      icon: <Award className="w-6 h-6 text-primary" />,
      badge: (stats: Stats) => `${stats.averageScore}`,
      value: (stats: Stats) => stats.averageScore,
      footerMain: "All quizzes",
    },
    {
      key: "bestWorstScore",
      description: "Extreme Results",
      icon: <AlertTriangle className="w-6 h-6 text-primary" />,
      badge: (stats: Stats) => (
        <div className="flex flex-col items-center gap-2">
          <Badge variant="success" className="mx-auto">Best: {stats.bestScore}</Badge>
          <Badge variant="destructive" className="mx-auto">Worst: {stats.worstScore}</Badge>
        </div>
      ),
      value: (stats: Stats) => `${stats.bestScore} / ${stats.worstScore}`,
      footerMain: "Best & Worst",
    },
    {
      key: "averageTimeSeconds",
      description: "Average Time",
      icon: <Clock className="w-6 h-6 text-primary" />,
      badge: (stats: Stats) => `${stats.averageTimeSeconds}s`,
      value: (stats: Stats) => stats.averageTimeSeconds,
      footerMain: "Avg. time per quiz",
    },
  ];

  return (
    <div className="grid grid-cols-1 gap-4 lg:grid-cols-2 xl:grid-cols-4">
      {cardConfig.map((item) => (
        <Card key={item.key}>
          <CardHeader>
            <div className="flex items-center justify-between">
              <CardDescription>{item.description}</CardDescription>
              {item.icon}
            </div>
            <CardTitle className="text-2xl font-semibold tabular-nums">
              {item.value(stats)}
            </CardTitle>
            
          </CardHeader>
          <CardFooter className="flex-col items-start gap-1.5 text-sm font-medium">
            {item.footerMain}
          </CardFooter>
        </Card>
      ))}
    </div>
  );
}