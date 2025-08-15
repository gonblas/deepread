"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Save } from "lucide-react";
import {
  useQuiz,
  type Question,
  type MultipleChoiceQuestion,
} from "@/contexts/quizContext";
import { QuizQuestionEditor } from "@/components/quizzes/QuizQuestionEditor";
import { QuizQuestionsList } from "@/components/quizzes/QuizQuestionsList";
import { QuizPreview } from "@/components/quizzes/QuizPreview";
import { SectionHeader } from "../SectionHeader";

interface QuizCreatorProps {
  onSave?: () => void;
  isEditing?: boolean;
}

export function QuizCreator({ onSave, isEditing = false }: QuizCreatorProps) {
  const {
    quiz,
    saving,
    addQuestion,
    updateQuestion,
    deleteQuestion,
    duplicateQuestion,
  } = useQuiz();

  const [currentQuestion, setCurrentQuestion] = useState<Question>({
    id: crypto.randomUUID(),
    type: "MULTIPLE_CHOICE",
    prompt: "",
    explanation: "",
    options: [
      { text: "", isCorrect: false },
      { text: "", isCorrect: false },
      { text: "", isCorrect: false },
      { text: "", isCorrect: false },
    ],
  } as MultipleChoiceQuestion);

  const [editingIndex, setEditingIndex] = useState<number | null>(null);
  const [activeTab, setActiveTab] = useState("editor");

  const resetCurrentQuestion = () => {
    setCurrentQuestion({
      id: crypto.randomUUID(),
      type: "MULTIPLE_CHOICE",
      prompt: "",
      explanation: "",
      options: [
        { text: "", isCorrect: false },
        { text: "", isCorrect: false },
        { text: "", isCorrect: false },
        { text: "", isCorrect: false },
      ],
    } as MultipleChoiceQuestion);
    setEditingIndex(null);
  };

  const validateQuestion = (question: Question): boolean => {
    if (!question.prompt.trim()) return false;

    switch (question.type) {
      case "TRUE_FALSE":
        return true;
      case "OPEN":
        return question.expectedAnswer?.trim() !== "";
      case "MULTIPLE_CHOICE":
        const mcQuestion = question as MultipleChoiceQuestion;
        return (
          mcQuestion.options.length >= 2 &&
          mcQuestion.options.every((opt) => opt.text.trim() !== "") &&
          mcQuestion.options.some((opt) => opt.isCorrect)
        );
    }
  };

  const handleSaveQuestion = () => {
    if (!validateQuestion(currentQuestion)) return;

    if (editingIndex !== null) {
      updateQuestion(editingIndex, currentQuestion);
    } else {
      addQuestion(currentQuestion);
    }

    resetCurrentQuestion();
    setActiveTab("questions");
  };

  const handleEditQuestion = (index: number) => {
    if (!quiz) return;
    setCurrentQuestion(quiz.questions[index]);
    setEditingIndex(index);
    setActiveTab("editor");
  };

  const handleDeleteQuestion = (index: number) => {
    deleteQuestion(index);
  };

  const handleDuplicateQuestion = (index: number) => {
    duplicateQuestion(index);
  };

  const canSaveQuiz = () => {
    return quiz && quiz.questions.length > 0;
  };

  const handleSaveQuiz = () => {
    if (canSaveQuiz()) {
      onSave?.();
    }
  };

  const questions = quiz?.questions || [];

  return (
    <div className="w-full mx-auto space-y-6">
      <SectionHeader
        title={isEditing ? "Edit Quiz" : "Create Quiz"}
        loading={saving}
        error={false}
        description={
          isEditing
            ? "Modify your quiz questions"
            : "Create engaging quizzes with multiple question types"
        }
      >
        <Button
          onClick={handleSaveQuiz}
          disabled={!canSaveQuiz() || saving}
          className="gap-2"
        >
          <Save className="w-4 h-4" />
          {saving ? "Saving..." : isEditing ? "Update Quiz" : "Save Quiz"}
        </Button>
      </SectionHeader>

      <Tabs
        value={activeTab}
        onValueChange={setActiveTab}
        className="space-y-6"
      >
        <TabsList className="grid w-full grid-cols-3">
          <TabsTrigger value="editor">Question Editor</TabsTrigger>
          <TabsTrigger value="questions">
            Questions ({questions.length})
          </TabsTrigger>
          <TabsTrigger value="preview">Preview</TabsTrigger>
        </TabsList>

        <TabsContent value="editor" className="space-y-6">
          <QuizQuestionEditor
            currentQuestion={currentQuestion}
            editingIndex={editingIndex}
            onQuestionChange={setCurrentQuestion}
            onSave={handleSaveQuestion}
            onCancel={resetCurrentQuestion}
            isValid={validateQuestion(currentQuestion)}
          />
        </TabsContent>

        <TabsContent value="questions" className="space-y-6">
          <QuizQuestionsList
            questions={questions}
            onEdit={handleEditQuestion}
            onDelete={handleDeleteQuestion}
            onDuplicate={handleDuplicateQuestion}
            onAddFirst={() => setActiveTab("editor")}
          />
        </TabsContent>

        <TabsContent value="preview" className="space-y-6">
          <QuizPreview
            questions={questions}
            onAddQuestions={() => setActiveTab("editor")}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
}
