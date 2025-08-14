"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Trash2, Edit, Copy, CheckCircle, List, MoreVertical, HelpCircle } from "lucide-react"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { motion, AnimatePresence } from "framer-motion"
import { cn } from "@/lib/utils"
import { EmptyState } from "@/components/ui/empty-state"
import type { Question, QuestionType } from "@/contexts/quizContext"

interface QuizQuestionsListProps {
  questions: Question[]
  onEdit: (index: number) => void
  onDelete: (index: number) => void
  onDuplicate: (index: number) => void
  onAddFirst: () => void
}

export function QuizQuestionsList({ questions, onEdit, onDelete, onDuplicate, onAddFirst }: QuizQuestionsListProps) {
  const getQuestionTypeIcon = (type: QuestionType) => {
    switch (type) {
      case "TRUE_FALSE":
        return <CheckCircle className="w-4 h-4" />
      case "OPEN":
        return <Edit className="w-4 h-4" />
      case "MULTIPLE_CHOICE":
        return <List className="w-4 h-4" />
    }
  }

  const getQuestionTypeLabel = (type: QuestionType) => {
    switch (type) {
      case "TRUE_FALSE":
        return "True/False"
      case "OPEN":
        return "Open Question"
      case "MULTIPLE_CHOICE":
        return "Multiple Choice"
    }
  }

  const getQuestionTypeColor = (type: QuestionType) => {
    switch (type) {
      case "TRUE_FALSE":
        return "bg-green-100 text-green-800 border-green-200"
      case "OPEN":
        return "bg-blue-100 text-blue-800 border-blue-200"
      case "MULTIPLE_CHOICE":
        return "bg-purple-100 text-purple-800 border-purple-200"
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>Quiz Questions ({questions.length})</CardTitle>
        <CardDescription>Manage all questions in your quiz</CardDescription>
      </CardHeader>
      <CardContent>
        {questions.length === 0 ? (
          <EmptyState
            icon={<HelpCircle className="w-12 h-12" />}
            title="No questions yet"
            description="Start building your quiz by adding questions"
            action={{
              label: "Add First Question",
              onClick: onAddFirst,
            }}
          />
        ) : (
          <div className="space-y-4">
            <AnimatePresence>
              {questions.map((question, index) => (
                <motion.div
                  key={question.id}
                  initial={{ opacity: 0, y: 20 }}
                  animate={{ opacity: 1, y: 0 }}
                  exit={{ opacity: 0, y: -20 }}
                  className="border rounded-lg p-4 hover:bg-muted/50 transition-colors"
                >
                  <div className="space-y-3">
                    {/* Header with question number and type - Always visible */}
                    <div className="flex items-start justify-between gap-3">
                      <div className="flex items-center gap-2 min-w-0 flex-1">
                        <span className="font-medium text-sm text-muted-foreground whitespace-nowrap">
                          Question {index + 1}
                        </span>
                        <Badge className={cn("gap-1 text-xs", getQuestionTypeColor(question.type))}>
                          {getQuestionTypeIcon(question.type)}
                          <span className="hidden sm:inline">{getQuestionTypeLabel(question.type)}</span>
                        </Badge>
                      </div>

                      {/* Actions - Responsive layout */}
                      <div className="flex items-center gap-2">
                        {/* Desktop actions */}
                        <div className="hidden sm:flex items-center gap-2">
                          <Button variant="outline" size="sm" onClick={() => onEdit(index)} className="gap-1">
                            <Edit className="w-3 h-3" />
                            Edit
                          </Button>
                          <Button variant="outline" size="sm" onClick={() => onDuplicate(index)} className="gap-1">
                            <Copy className="w-3 h-3" />
                            Copy
                          </Button>
                          <AlertDialog>
                            <AlertDialogTrigger asChild>
                              <Button
                                variant="outline"
                                size="sm"
                                className="gap-1 text-red-600 hover:text-red-700 bg-transparent"
                              >
                                <Trash2 className="w-3 h-3" />
                                Delete
                              </Button>
                            </AlertDialogTrigger>
                            <AlertDialogContent>
                              <AlertDialogHeader>
                                <AlertDialogTitle>Delete Question</AlertDialogTitle>
                                <AlertDialogDescription>
                                  Are you sure you want to delete this question? This action cannot be undone.
                                </AlertDialogDescription>
                              </AlertDialogHeader>
                              <AlertDialogFooter>
                                <AlertDialogCancel>Cancel</AlertDialogCancel>
                                <AlertDialogAction
                                  onClick={() => onDelete(index)}
                                  className="bg-red-600 hover:bg-red-700"
                                >
                                  Delete
                                </AlertDialogAction>
                              </AlertDialogFooter>
                            </AlertDialogContent>
                          </AlertDialog>
                        </div>

                        {/* Mobile dropdown menu */}
                        <div className="sm:hidden">
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="outline" size="sm">
                                <MoreVertical className="w-4 h-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              <DropdownMenuItem onClick={() => onEdit(index)}>
                                <Edit className="w-4 h-4 mr-2" />
                                Edit
                              </DropdownMenuItem>
                              <DropdownMenuItem onClick={() => onDuplicate(index)}>
                                <Copy className="w-4 h-4 mr-2" />
                                Copy
                              </DropdownMenuItem>
                              <AlertDialog>
                                <AlertDialogTrigger asChild>
                                  <DropdownMenuItem
                                    className="text-red-600 focus:text-red-600"
                                    onSelect={(e) => e.preventDefault()}
                                  >
                                    <Trash2 className="w-4 h-4 mr-2" />
                                    Delete
                                  </DropdownMenuItem>
                                </AlertDialogTrigger>
                                <AlertDialogContent>
                                  <AlertDialogHeader>
                                    <AlertDialogTitle>Delete Question</AlertDialogTitle>
                                    <AlertDialogDescription>
                                      Are you sure you want to delete this question? This action cannot be undone.
                                    </AlertDialogDescription>
                                  </AlertDialogHeader>
                                  <AlertDialogFooter>
                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                    <AlertDialogAction
                                      onClick={() => onDelete(index)}
                                      className="bg-red-600 hover:bg-red-700"
                                    >
                                      Delete
                                    </AlertDialogAction>
                                  </AlertDialogFooter>
                                </AlertDialogContent>
                              </AlertDialog>
                            </DropdownMenuContent>
                          </DropdownMenu>
                        </div>
                      </div>
                    </div>

                    {/* Question content */}
                    <div className="space-y-2">
                      <p className="font-medium text-sm sm:text-base leading-relaxed">{question.prompt}</p>
                      {question.explanation && (
                        <p className="text-sm text-muted-foreground leading-relaxed">{question.explanation}</p>
                      )}
                    </div>
                  </div>
                </motion.div>
              ))}
            </AnimatePresence>
          </div>
        )}
      </CardContent>
    </Card>
  )
}
