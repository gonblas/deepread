"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

function timeAgo(dateString: string) {
  const now = new Date();
  const date = new Date(dateString);
  const diffMs = now.getTime() - date.getTime();
  const diffSec = Math.floor(diffMs / 1000);
  const diffMin = Math.floor(diffSec / 60);
  const diffHour = Math.floor(diffMin / 60);
  const diffDay = Math.floor(diffHour / 24);

  if (diffDay > 0) return `${diffDay} day${diffDay > 1 ? "s" : ""} ago`;
  if (diffHour > 0) return `${diffHour} hour${diffHour > 1 ? "s" : ""} ago`;
  if (diffMin > 0) return `${diffMin} minute${diffMin > 1 ? "s" : ""} ago`;
  return "a few seconds ago";
}

export function RecentAttemptsTable({ recentAttempts }: { recentAttempts: Array<{
  id: string;
  bookTitle: string;
  chapterNumber: number;
  score: number;
  date: string;
}> }
) {
  return (
    <Card>
      <CardHeader>
        <CardTitle>Recent Activity</CardTitle>
        <CardDescription>Latest quiz attempts</CardDescription>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Book</TableHead>
              <TableHead>Chapter Number</TableHead>
              <TableHead>Score</TableHead>
              <TableHead>Time</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {recentAttempts.map((attempt) => (
              <TableRow key={attempt.id}>
                <TableCell className="max-w-[200px] truncate">{attempt.bookTitle}</TableCell>
                <TableCell>{attempt.chapterNumber}</TableCell>
                <TableCell>{attempt.score}</TableCell>
                <TableCell className="text-muted-foreground">
                  {timeAgo(attempt.date)}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}