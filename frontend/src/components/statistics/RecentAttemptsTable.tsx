"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { RecentAttempt } from "@/contexts/recentAttemptsContext";
import { formatDistanceToNow } from "date-fns"

export function RecentAttemptsTable({ recentAttempts, loading, error }: { recentAttempts: Array<RecentAttempt>, loading: boolean, error: string | null }
) {
  if(loading) {
    return <div className="p-4">Loading recent attempts...</div>
  }
  if(error) {
    return <div className="p-4 text-red-500">Error loading recent attempts: {error}</div>
  }

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
                  {formatDistanceToNow(attempt.submittedAt)}
                </TableCell>
              </TableRow>
            ))}
            {recentAttempts.length === 0 && (
              <TableRow>
                <TableCell colSpan={4} className="font-bold text-md text-center py-20">
                  No recent attempts found. 
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  )
}