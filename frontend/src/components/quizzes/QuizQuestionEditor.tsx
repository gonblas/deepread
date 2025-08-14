"use client"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Badge } from "@/components/ui/badge"
import { Plus, Trash2, Edit, Save, CheckCircle, XCircle, List, RotateCcw } from "lucide-react"
import { motion, AnimatePresence } from "framer-motion"
import { cn } from "@/lib/utils"
import type {
  Question,
  QuestionType,
  MultipleChoiceQuestion,
  TrueFalseQuestion,
  OpenQuestion,
} from "@/contexts/quizContext"

interface QuizQuestionEditorProps {
  currentQuestion: Question
  editingIndex: number | null
  onQuestionChange: (question: Question) => void
  onSave: () => void
  onCancel: () => void
  isValid: boolean
}

export function QuizQuestionEditor({
  currentQuestion,
  editingIndex,
  onQuestionChange,
  onSave,
  onCancel,
  isValid,
}: QuizQuestionEditorProps) {
  const handleQuestionTypeChange = (type: QuestionType) => {
    const baseQuestion = {
      id: currentQuestion.id,
      type,
      prompt: currentQuestion.prompt,
      explanation: currentQuestion.explanation,
    }

    switch (type) {
      case "TRUE_FALSE":
        onQuestionChange({
          ...baseQuestion,
          type: "TRUE_FALSE",
          isAnswerTrue: true,
        } as TrueFalseQuestion)
        break
      case "OPEN":
        onQuestionChange({
          ...baseQuestion,
          type: "OPEN",
          expectedAnswer: "",
        } as OpenQuestion)
        break
      case "MULTIPLE_CHOICE":
        onQuestionChange({
          ...baseQuestion,
          type: "MULTIPLE_CHOICE",
          options: [
            { text: "", isCorrect: false },
            { text: "", isCorrect: false },
            { text: "", isCorrect: false },
            { text: "", isCorrect: false },
          ],
        } as MultipleChoiceQuestion)
        break
    }
  }

  const updateMultipleChoiceOption = (index: number, text: string) => {
    if (currentQuestion.type === "MULTIPLE_CHOICE") {
      const newOptions = [...currentQuestion.options]
      newOptions[index] = { ...newOptions[index], text }
      onQuestionChange({ ...currentQuestion, options: newOptions })
    }
  }

  const setCorrectMultipleChoiceOption = (index: number) => {
    if (currentQuestion.type === "MULTIPLE_CHOICE") {
      const newOptions = currentQuestion.options.map((option, i) => ({
        ...option,
        isCorrect: i === index,
      }))
      onQuestionChange({ ...currentQuestion, options: newOptions })
    }
  }

  const addMultipleChoiceOption = () => {
    if (currentQuestion.type === "MULTIPLE_CHOICE") {
      const newOptions = [...currentQuestion.options, { text: "", isCorrect: false }]
      onQuestionChange({ ...currentQuestion, options: newOptions })
    }
  }

  const removeMultipleChoiceOption = (index: number) => {
    if (currentQuestion.type === "MULTIPLE_CHOICE" && currentQuestion.options.length > 2) {
      const newOptions = currentQuestion.options.filter((_, i) => i !== index)
      onQuestionChange({ ...currentQuestion, options: newOptions })
    }
  }

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

  const renderQuestionEditor = () => {
    switch (currentQuestion.type) {
      case "TRUE_FALSE":
        return (
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-4">
            <div className="space-y-3">
              <Label className="text-base font-medium">Correct Answer</Label>
              <RadioGroup
                value={(currentQuestion as TrueFalseQuestion).isAnswerTrue.toString()}
                onValueChange={(value) =>
                  onQuestionChange({
                    ...currentQuestion,
                    isAnswerTrue: value === "true",
                  } as TrueFalseQuestion)
                }
                className="flex gap-6"
              >
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="true" id="true" />
                  <Label htmlFor="true" className="flex items-center gap-2 cursor-pointer">
                    <CheckCircle className="w-4 h-4 text-green-600" />
                    True
                  </Label>
                </div>
                <div className="flex items-center space-x-2">
                  <RadioGroupItem value="false" id="false" />
                  <Label htmlFor="false" className="flex items-center gap-2 cursor-pointer">
                    <XCircle className="w-4 h-4 text-red-600" />
                    False
                  </Label>
                </div>
              </RadioGroup>
            </div>
          </motion.div>
        )

      case "OPEN":
        return (
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="expectedAnswer" className="text-base font-medium">
                Expected Answer
              </Label>
              <Textarea
                id="expectedAnswer"
                value={(currentQuestion as OpenQuestion).expectedAnswer}
                onChange={(e) =>
                  onQuestionChange({
                    ...currentQuestion,
                    expectedAnswer: e.target.value,
                  } as OpenQuestion)
                }
                placeholder="Enter the expected answer or key points..."
                rows={4}
                className="resize-none"
              />
              <p className="text-sm text-muted-foreground">
                This will be used as a reference for manual grading or automatic keyword matching.
              </p>
            </div>
          </motion.div>
        )

      case "MULTIPLE_CHOICE":
        const mcQuestion = currentQuestion as MultipleChoiceQuestion
        return (
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-4">
            <div className="space-y-3">
              <div className="flex items-center justify-between">
                <Label className="text-base font-medium">Answer Options</Label>
                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={addMultipleChoiceOption}
                  className="gap-2 bg-transparent"
                >
                  <Plus className="w-4 h-4" />
                  Add Option
                </Button>
              </div>

              <div className="space-y-3">
                <AnimatePresence>
                  {mcQuestion.options.map((option, index) => (
                    <motion.div
                      key={index}
                      initial={{ opacity: 0, x: -20 }}
                      animate={{ opacity: 1, x: 0 }}
                      exit={{ opacity: 0, x: 20 }}
                      className={cn(
                        "flex items-center gap-3 p-3 rounded-lg border-2 transition-all",
                        option.isCorrect
                          ? "border-green-700"
                          : "border-border bg-background hover:bg-muted/50",
                      )}
                    >
                      <RadioGroup
                        value={mcQuestion.options.findIndex((opt) => opt.isCorrect).toString()}
                        onValueChange={(value) => setCorrectMultipleChoiceOption(Number.parseInt(value))}
                      >
                        <RadioGroupItem value={index.toString()} />
                      </RadioGroup>

                      <Input
                        value={option.text}
                        onChange={(e) => updateMultipleChoiceOption(index, e.target.value)}
                        placeholder={`Option ${index + 1}`}
                        className="flex-1 border-none shadow-none focus-visible:ring-0"
                      />

                      <div className="flex items-center gap-2">
                        {mcQuestion.options.length > 2 && (
                          <Button
                            type="button"
                            variant="ghost"
                            size="sm"
                            onClick={() => removeMultipleChoiceOption(index)}
                            className="text-red-600 hover:text-red-700 hover:bg-red-50"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        )}
                      </div>
                    </motion.div>
                  ))}
                </AnimatePresence>
              </div>

              <p className="text-sm text-muted-foreground">
                Click the radio button next to an option to mark it as correct.
              </p>
            </div>
          </motion.div>
        )
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          {editingIndex !== null ? <Edit className="w-5 h-5" /> : <Plus className="w-5 h-5" />}
          {editingIndex !== null ? "Edit Question" : "Add New Question"}
        </CardTitle>
        <CardDescription>
          {editingIndex !== null ? "Modify the selected question" : "Create a new question for your quiz"}
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label>Question Type</Label>
            <Select
              value={currentQuestion.type}
              onValueChange={(value: QuestionType) => handleQuestionTypeChange(value)}
            >
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="MULTIPLE_CHOICE">
                  <div className="flex items-center gap-2">
                    <List className="w-4 h-4" />
                    Multiple Choice
                  </div>
                </SelectItem>
                <SelectItem value="TRUE_FALSE">
                  <div className="flex items-center gap-2">
                    <CheckCircle className="w-4 h-4" />
                    True/False
                  </div>
                </SelectItem>
                <SelectItem value="OPEN">
                  <div className="flex items-center gap-2">
                    <Edit className="w-4 h-4" />
                    Open Question
                  </div>
                </SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        <div className="space-y-2">
          <Label htmlFor="prompt">Question *</Label>
          <Textarea
            id="prompt"
            value={currentQuestion.prompt}
            onChange={(e) => onQuestionChange({ ...currentQuestion, prompt: e.target.value })}
            placeholder="Enter your question here..."
            rows={3}
          />
        </div>

        {renderQuestionEditor()}

        <div className="space-y-2">
          <Label htmlFor="explanation">Explanation (Optional)</Label>
          <Textarea
            id="explanation"
            value={currentQuestion.explanation}
            onChange={(e) => onQuestionChange({ ...currentQuestion, explanation: e.target.value })}
            placeholder="Explain the correct answer or provide additional context..."
            rows={2}
          />
        </div>

        <div className="flex gap-3 pt-4">
          <Button onClick={onSave} disabled={!isValid} className="gap-2">
            {editingIndex !== null ? <Save className="w-4 h-4" /> : <Plus className="w-4 h-4" />}
            {editingIndex !== null ? "Update Question" : "Add Question"}
          </Button>
          {editingIndex !== null && (
            <Button variant="outline" onClick={onCancel} className="gap-2 bg-transparent">
              <RotateCcw className="w-4 h-4" />
              Cancel
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  )
}
