"use client";

import {
  createContext,
  useContext,
  useState,
  useCallback,
  type ReactNode,
} from "react";
import Cookies from "js-cookie";
import { useNotification } from "./notificationContext";
import { useDataRefresh } from "./dataRefreshContext";
import { useNavigate } from "react-router-dom";

export type QuestionType = "TRUE_FALSE" | "OPEN" | "MULTIPLE_CHOICE";

interface MultipleChoiceOption {
  text: string;
  isCorrect: boolean;
}

interface BaseQuestion {
  id: string;
  type: QuestionType;
  prompt: string;
  explanation: string;
}

export interface TrueFalseQuestion extends BaseQuestion {
  type: "TRUE_FALSE";
  isAnswerTrue: boolean;
}

export interface OpenQuestion extends BaseQuestion {
  type: "OPEN";
  expectedAnswer: string;
}

export interface MultipleChoiceQuestion extends BaseQuestion {
  type: "MULTIPLE_CHOICE";
  options: MultipleChoiceOption[];
}

export type Question =
  | TrueFalseQuestion
  | OpenQuestion
  | MultipleChoiceQuestion;

export interface Quiz {
  id?: string;
  chapter?: Chapter;
  questions: Question[];
}

export interface Chapter {
  id: string;
  title: string;
  number: number;
  summary: string;
}

interface QuizContextType {
  // State
  quiz: Quiz | null;
  loading: boolean;
  error: string | null;
  saving: boolean;

  // Actions
  createQuiz: (
    bookId: string,
    chapterId: string,
    questions: Question[]
  ) => Promise<void>;
  updateQuiz: (id: string, quiz: Quiz) => Promise<void>;
  fetchQuiz: (id: string) => Promise<void>;
  resetQuiz: () => void;

  // Quiz manipulation
  setQuiz: (quiz: Quiz) => void;
  addQuestion: (question: Question) => void;
  updateQuestion: (index: number, question: Question) => void;
  deleteQuestion: (index: number) => void;
  duplicateQuestion: (index: number) => void;
}

const QuizContext = createContext<QuizContextType | undefined>(undefined);

export function useQuiz() {
  const context = useContext(QuizContext);
  if (context === undefined) {
    throw new Error("useQuiz must be used within a QuizProvider");
  }
  return context;
}

interface QuizProviderProps {
  children: ReactNode;
}

export function QuizProvider({ children }: QuizProviderProps) {
  const [quiz, setQuizState] = useState<Quiz | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);
  const { createResource } = useDataRefresh();
  const { showErrorFromHttpResponse } = useNotification();
  const navigate = useNavigate();

  const setQuiz = useCallback((newQuiz: Quiz) => {
    setQuizState(newQuiz);
    setError(null);
  }, []);

  const resetQuiz = useCallback(() => {
    setQuizState(null);
    setError(null);
    setLoading(false);
    setSaving(false);
  }, []);

  const createQuiz = useCallback(
    async (bookId: string, chapterId: string, questions: Question[]) => {
      setSaving(true);
      setError(null);

      // Función separada que hace la petición
      const createQuizRequest = async () => {
        const response = await fetch(
          `http://localhost:8080/api/books/${bookId}/chapters/${chapterId}/quiz`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${Cookies.get("token")}`,
            },
            body: JSON.stringify({
              questions: questions.map((q) => ({
                id: q.id,
                type: q.type,
                prompt: q.prompt,
                explanation: q.explanation,
                ...(q.type === "TRUE_FALSE" && {
                  isAnswerTrue: (q as TrueFalseQuestion).isAnswerTrue,
                }),
                ...(q.type === "OPEN" && {
                  expectedAnswer: (q as OpenQuestion).expectedAnswer,
                }),
                ...(q.type === "MULTIPLE_CHOICE" && {
                  options: (q as MultipleChoiceQuestion).options.map((opt) => ({
                    text: opt.text,
                    isCorrect: opt.isCorrect,
                  })),
                }),
              })),
            }),
          }
        );

        if (!response.ok) {
          showErrorFromHttpResponse(
            "Failed to create quiz",
            await response.json()
          );
          throw new Error("Failed to create quiz");
        }

        const createdQuiz = await response.json();
        setQuizState(createdQuiz);
        return createdQuiz;
      };

      try {
        await createResource(
          "quiz",
          createQuizRequest,
          "Quiz created successfully",
          // Redirección usando el quizId
          `/quizzes/${(await createQuizRequest()).id}`
        );
      } catch (err) {
        const errorMessage =
          err instanceof Error ? err.message : "Failed to create quiz";
        setError(errorMessage);
        throw new Error(errorMessage);
      } finally {
        setSaving(false);
      }
    },
    [createResource]
  );

  const updateQuiz = useCallback(async (id: string, quizData: Quiz) => {
    setSaving(true);
    setError(null);

    try {
      // Simulate API call - replace with actual endpoint
      await new Promise((resolve) => setTimeout(resolve, 1500));

      const response = await fetch(`/api/quizzes/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          questions: quizData.questions.map((q) => ({
            id: q.id,
            type: q.type,
            prompt: q.prompt,
            explanation: q.explanation,
            ...(q.type === "TRUE_FALSE" && {
              isAnswerTrue: (q as TrueFalseQuestion).isAnswerTrue,
            }),
            ...(q.type === "OPEN" && {
              expectedAnswer: (q as OpenQuestion).expectedAnswer,
            }),
            ...(q.type === "MULTIPLE_CHOICE" && {
              options: (q as MultipleChoiceQuestion).options.map((opt) => ({
                text: opt.text,
                isCorrect: opt.isCorrect,
              })),
            }),
          })),
        }),
      });

      if (!response.ok) {
        throw new Error("Failed to update quiz");
      }

      const updatedQuiz = await response.json();
      setQuizState(updatedQuiz);

      console.log("Quiz updated successfully:", updatedQuiz);
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to update quiz";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setSaving(false);
    }
  }, []);

  const fetchQuiz = useCallback(async (id: string) => {
    setLoading(true);
    setError(null);

    try {
      const response = await fetch(`http://localhost:8080/api/quizzes/${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      });

      if (!response.ok) {
        showErrorFromHttpResponse(
          "Failed to fetch quiz",
          await response.json()
        );
        throw new Error("Failed to create quiz");
      }

      const quiz = await response.json();
      setQuizState(quiz);
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to fetch quiz";
      setError(errorMessage);
      throw new Error(errorMessage);
    } finally {
      setLoading(false);
    }
  }, []);

  const addQuestion = useCallback((question: Question) => {
    setQuizState((prev) => {
      if (!prev) return { questions: [question] };
      return { ...prev, questions: [...prev.questions, question] };
    });
  }, []);

  const updateQuestion = useCallback((index: number, question: Question) => {
    setQuizState((prev) => {
      if (!prev) return null;
      const newQuestions = [...prev.questions];
      newQuestions[index] = question;
      return { ...prev, questions: newQuestions };
    });
  }, []);

  const deleteQuestion = useCallback((index: number) => {
    setQuizState((prev) => {
      if (!prev) return null;
      const newQuestions = prev.questions.filter((_, i) => i !== index);
      return { ...prev, questions: newQuestions };
    });
  }, []);

  const duplicateQuestion = useCallback((index: number) => {
    setQuizState((prev) => {
      if (!prev) return null;
      const questionToDuplicate = {
        ...prev.questions[index],
        id: crypto.randomUUID(),
      };
      const newQuestions = [...prev.questions];
      newQuestions.splice(index + 1, 0, questionToDuplicate);
      return { ...prev, questions: newQuestions };
    });
  }, []);

  const value: QuizContextType = {
    quiz,
    loading,
    error,
    saving,
    createQuiz,
    updateQuiz,
    fetchQuiz,
    resetQuiz,
    setQuiz,
    addQuestion,
    updateQuestion,
    deleteQuestion,
    duplicateQuestion,
  };

  return <QuizContext.Provider value={value}>{children}</QuizContext.Provider>;
}
