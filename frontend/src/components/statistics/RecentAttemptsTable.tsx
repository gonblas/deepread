"use client"

import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

const recentAttempts = [
  {
    id: 1,
    bookTitle: "One Hundred Years of Solitude",
    chapterNumber: 1,
    score: 85,
    date: "2025-08-09T09:00:00"
  },
  {
    id: 2,
    bookTitle: "Don Quixote",
    chapterNumber: 2,
    score: 72,
    date: "2025-08-09T07:00:00"
  },
  {
    id: 3,
    bookTitle: "Hopscotch",
    chapterNumber: 3,
    score: 45,
    date: "2025-08-08T10:00:00"
  },
  {
    id: 4,
    bookTitle: "The House of the Spirits",
    chapterNumber: 4,
    score: 88,
    date: "2025-08-08T09:00:00"
  },
  {
    id: 5,
    bookTitle: "Love in the Time of Cholera",
    chapterNumber: 2,
    score: 91,
    date: "2025-08-07T10:00:00"
  },
]

// "Time ago" function in English
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

export function RecentAttemptsTable() {
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