"use client";

import type React from "react";

import { useState, useRef, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Save,
  FileText,
  Clock,
  MessageCircleQuestionMark,
  MoreHorizontal,
  Edit3,
  Sparkles,
  Check,
  Loader2,
  Download,
} from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { cn } from "@/lib/utils";
import { motion, AnimatePresence } from "framer-motion";
import { Label } from "@/components/ui/label";
import { DeleteChapterDialog } from "./DeleteChapterDialog";
import { useNavigate, useParams } from "react-router-dom";
import Cookies from "js-cookie";
import { useNotification } from "@/contexts/notificationContext";
import { DeleteElementDialog } from "../DeleteElementDialog";
interface ModernChapterEditorProps {
  title: string;
  summary: string;
  number: number;
  onTitleChange: (title: string) => void;
  onSummaryChange: (summary: string) => void;
  onNumberChange: (number: number) => void;
  onSave: (chapter: {
    title: string;
    summary: string;
    number: number;
  }) => Promise<boolean>;
  isNew?: boolean;
  onExport?: () => void;
  isSaving?: boolean;
}

export function ModernChapterEditor({
  title,
  summary,
  number,
  onTitleChange,
  onSummaryChange,
  onNumberChange,
  onSave,
  isNew = false,
  onExport,
  isSaving = false,
}: ModernChapterEditorProps) {
  const [isEditing, setIsEditing] = useState(isNew);
  const [localTitle, setLocalTitle] = useState(title);
  const [localSummary, setLocalSummary] = useState(summary);
  const [localNumber, setLocalNumber] = useState(number);
  const [lastSaved, setLastSaved] = useState<Date | null>(null);
  const [hasUnsavedChanges, setHasUnsavedChanges] = useState(false);
  const textareaRef = useRef<HTMLTextAreaElement>(null);
  const titleRef = useRef<HTMLInputElement>(null);
  const { bookId, chapterId } = useParams();
  const { showWarning } = useNotification();
  const navigate = useNavigate();

  const handleQuizClick = async () => {
    if (!bookId || !chapterId) return;

    try {
      const response = await fetch(
        `http://localhost:8080/api/books/${bookId}/chapters/${chapterId}/quiz`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        const quizId = data.id;
        navigate(`/quizzes/${quizId}`);
      } else if (response.status === 404) {
        showWarning("No quiz found for this chapter. You can create one now.");
        navigate(`/books/${bookId}/chapters/${chapterId}/quiz/create`);
      } else {
        console.error("Error checking quiz", response.status);
      }
    } catch (err) {
      console.error("Error connecting to server", err);
    }
  };

  useEffect(() => {
    setLocalTitle(title);
    setLocalSummary(summary);
    setLocalNumber(number);
  }, [title, summary, number]);

  // Auto-resize textarea with smooth animation
  useEffect(() => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto";
      textarea.style.height = `${Math.max(textarea.scrollHeight, 200)}px`;
    }
  }, [localSummary]);

  // Track unsaved changes
  useEffect(() => {
    const hasChanges =
      localTitle !== title ||
      localSummary !== summary ||
      localNumber !== number;
    setHasUnsavedChanges(hasChanges);
  }, [localTitle, localSummary, localNumber, title, summary, number]);

  // Auto-save functionality (simulated)
  useEffect(() => {
    if (hasUnsavedChanges && !isNew) {
      const timer = setTimeout(() => {
        handleSave();
      }, 3000);

      return () => clearTimeout(timer);
    }
  }, [hasUnsavedChanges, localTitle, localSummary, localNumber]);

  const handleSave = async () => {
    const chapterData = {
      title: localTitle,
      summary: localSummary,
      number: localNumber,
    };

    const success = await onSave(chapterData);

    if (success) {
      // Only update parent state and exit edit mode if save was successful
      onTitleChange(localTitle);
      onSummaryChange(localSummary);
      onNumberChange(localNumber);
      setLastSaved(new Date());
      setHasUnsavedChanges(false);
      if (!isNew) {
        setIsEditing(false);
      }
    }
    // If save failed, stay in edit mode with current local values
  };

  const handleCancel = () => {
    setLocalTitle(title);
    setLocalSummary(summary);
    setLocalNumber(number);
    setHasUnsavedChanges(false);
    setIsEditing(false);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if ((e.metaKey || e.ctrlKey) && e.key === "s") {
      e.preventDefault();
      handleSave();
    }
    if ((e.metaKey || e.ctrlKey) && e.key === "e") {
      e.preventDefault();
      setIsEditing(!isEditing);
    }
  };

  const renderMarkdown = (text: string) => {
    return text
      .replace(
        /^### (.*$)/gim,
        '<h3 class="text-2xl font-bold mt-12 mb-6 text-foreground bg-gradient-to-r from-primary/20 to-transparent -mx-4 px-4 py-3 rounded-lg">$1</h3>'
      )
      .replace(
        /^## (.*$)/gim,
        '<h2 class="text-3xl font-bold mt-16 mb-8 text-foreground">$1</h2>'
      )
      .replace(
        /^# (.*$)/gim,
        '<h1 class="text-4xl font-bold mt-20 mb-10 text-foreground">$1</h1>'
      )
      .replace(
        /\*\*(.*?)\*\*/g,
        '<strong class="font-bold text-foreground bg-primary/10 px-1 rounded">$1</strong>'
      )
      .replace(/\*(.*?)\*/g, '<em class="italic text-primary">$1</em>')
      .replace(
        /`(.*?)`/g,
        '<code class="bg-muted/80 px-2 py-1 rounded-md text-sm font-mono border">$1</code>'
      )
      .replace(
        /^> (.*$)/gim,
        '<blockquote class="border-l-4 border-primary/50 pl-6 py-4 my-8 bg-gradient-to-r from-primary/5 to-transparent rounded-r-xl"><p class="italic text-muted-foreground text-lg">$1</p></blockquote>'
      )
      .replace(
        /^- (.*$)/gim,
        '<li class="ml-6 mb-3 list-disc marker:text-primary">$1</li>'
      )
      .replace(
        /^\d+\. (.*$)/gim,
        '<li class="ml-6 mb-3 list-decimal marker:text-primary marker:font-bold">$1</li>'
      )
      .replace(
        /\[([^\]]+)\]$$([^)]+)$$/g,
        '<a href="$2" class="text-primary underline decoration-2 underline-offset-4 hover:decoration-primary/50 transition-colors">$1</a>'
      )
      .replace(
        /\n\n/g,
        "</p><p class='mb-6 leading-relaxed text-foreground/90 text-lg'>"
      )
      .replace(/\n/g, "<br />");
  };

  const wordCount = localSummary
    .split(/\s+/)
    .filter((word) => word.length > 0).length;
  const readingTime = Math.ceil(wordCount / 200);

  return (
    <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20">
      {/* Floating Header */}
      <motion.div
        initial={{ y: -100, opacity: 0 }}
        animate={{ y: 0, opacity: 1 }}
        className="sticky top-0 z-50 backdrop-blur-xl bg-background/80 border-b border-border/50"
      >
        <div className="mx-auto py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="flex items-center gap-6 text-sm text-muted-foreground">
                <motion.span
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.3 }}
                  className="flex items-center gap-2"
                >
                  <FileText className="h-4 w-4" />
                  {wordCount.toLocaleString()} words
                </motion.span>
                <motion.span
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  transition={{ delay: 0.4 }}
                  className="flex items-center gap-2"
                >
                  <Clock className="h-4 w-4" />
                  {readingTime} min read
                </motion.span>
              </div>
            </div>

            <div className="flex items-center gap-3">
              {/* Save Status */}
              <AnimatePresence>
                {hasUnsavedChanges && (
                  <motion.div
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    exit={{ opacity: 0, scale: 0.8 }}
                    className="flex items-center gap-2 text-sm text-muted-foreground"
                  >
                    <div className="w-2 h-2 bg-orange-500 rounded-full animate-pulse" />
                    Unsaved changes
                  </motion.div>
                )}
                {lastSaved && !hasUnsavedChanges && (
                  <motion.div
                    initial={{ opacity: 0, scale: 0.8 }}
                    animate={{ opacity: 1, scale: 1 }}
                    exit={{ opacity: 0, scale: 0.8 }}
                    className="flex items-center gap-2 text-sm text-green-600"
                  >
                    <Check className="w-4 h-4" />
                    Saved
                  </motion.div>
                )}
              </AnimatePresence>

              {/* Action Buttons */}
              <div className="flex items-center gap-2">
                {isEditing ? (
                  <motion.div
                    initial={{ opacity: 0, x: 20 }}
                    animate={{ opacity: 1, x: 0 }}
                    className="flex gap-2"
                  >
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={handleCancel}
                      className="bg-background/50"
                    >
                      Cancel
                    </Button>
                    <Button
                      size="sm"
                      onClick={handleSave}
                      disabled={isSaving}
                      className="bg-primary hover:bg-primary/90 shadow-lg shadow-primary/25"
                    >
                      {isSaving ? (
                        <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                      ) : (
                        <Save className="w-4 h-4 mr-2" />
                      )}
                      Save
                    </Button>
                  </motion.div>
                ) : (
                  <motion.div
                    initial={{ opacity: 0, x: 20 }}
                    animate={{ opacity: 1, x: 0 }}
                    className="flex gap-2"
                  >
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setIsEditing(true)}
                      className="bg-background/50 hover:bg-primary/10 hover:border-primary/20"
                    >
                      <Edit3 className="size-4 mr-2" />
                      Edit
                    </Button>
                    {!isNew && (
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={handleQuizClick}
                        className="bg-background/50 hover:bg-primary/10 hover:border-primary/20"
                      >
                        <MessageCircleQuestionMark className="size-5 mr-1" />
                        Quiz
                      </Button>
                    )}
                    {!isNew && bookId && chapterId && (
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
                          <DropdownMenuLabel>Chapter Actions</DropdownMenuLabel>
                          <DropdownMenuItem
                            onClick={onExport}
                            className="w-full gap-2 flex justify-center"
                          >
                            <Download className="size-4" />
                            Export as Markdown
                          </DropdownMenuItem>
                          <DropdownMenuSeparator />
                          <DeleteElementDialog
                            deleteURL={`http://localhost:8080/api/books/${bookId}/chapters/${chapterId}`}
                            redirectURL={`/books/${bookId}`}
                            resourceType="chapter"
                            resourceName={title}
                            fullWidth={true}
                          />
                        </DropdownMenuContent>
                      </DropdownMenu>
                    )}
                  </motion.div>
                )}
              </div>
            </div>
          </div>
        </div>
      </motion.div>

      {/* Main Content */}
      <div className="mx-auto px-6 py-12" onKeyDown={handleKeyDown}>
        {/* Chapter Number and Title Section */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.1 }}
          className="mb-16 space-y-6"
        >
          {/* Chapter Number */}
          {isEditing ? (
            <div className="flex items-center gap-4">
              <Label
                htmlFor="chapterNumber"
                className="text-lg font-medium text-muted-foreground"
              >
                Chapter Number:
              </Label>
              <Input
                id="chapterNumber"
                type="number"
                value={localNumber}
                onChange={(e) =>
                  setLocalNumber(Number.parseInt(e.target.value) || 0)
                }
                min="1"
                className="w-24 text-lg font-semibold"
              />
            </div>
          ) : (
            <div className="text-lg text-muted-foreground">
              Chapter {localNumber}
            </div>
          )}

          {/* Title */}
          {isEditing ? (
            <Input
              ref={titleRef}
              value={localTitle}
              onChange={(e) => setLocalTitle(e.target.value)}
              placeholder="Chapter title..."
              className="text-5xl font-bold border-none px-0 py-6 h-auto bg-transparent text-foreground placeholder:text-muted-foreground/50 focus-visible:ring-0 focus-visible:ring-offset-0 shadow-none"
              style={{ fontSize: "3rem", lineHeight: "1.1" }}
            />
          ) : (
            <motion.h1
              className={cn(
                "text-5xl font-bold text-foreground cursor-pointer transition-all duration-300",
                "hover:bg-gradient-to-r hover:from-muted/50 hover:to-transparent",
                "px-4 py-6 -mx-4 rounded-xl",
                !localTitle && "text-muted-foreground/50"
              )}
              onClick={() => setIsEditing(true)}
              whileHover={{ scale: 1.01 }}
              whileTap={{ scale: 0.99 }}
            >
              {localTitle || "Untitled Chapter"}
              {!localTitle && (
                <motion.span
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="ml-4 text-2xl"
                >
                  <Sparkles className="inline w-8 h-8 text-primary/50" />
                </motion.span>
              )}
            </motion.h1>
          )}
        </motion.div>

        {/* Content Section (Summary as Markdown) */}
        <motion.div
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.2 }}
          className="relative"
        >
          <h2 className="text-2xl font-semibold mb-6 text-foreground">
            Summary
          </h2>

          {isEditing ? (
            <div className="space-y-6">
              <div className="relative">
                <textarea
                  ref={textareaRef}
                  value={localSummary}
                  onChange={(e) => setLocalSummary(e.target.value)}
                  placeholder="Start writing your chapter content in markdown...
                              You can use markdown:
                              # Heading 1
                              ## Heading 2  
                              ### Heading 3

                              **Bold text**
                              *Italic text*
                              `Code`

                              > Quote

                              - List item
                              1. Numbered list

                              [Link](url)"
                  className={cn(
                    "w-full min-h-[600px] resize-none border-none bg-transparent",
                    "text-foreground placeholder:text-muted-foreground/50 focus:outline-none",
                    "text-lg leading-relaxed p-6 rounded-xl",
                    "bg-gradient-to-br from-background to-muted/20",
                    "shadow-inner border border-border/50",
                    // Maintain consistent width
                    "max-w-none"
                  )}
                  style={{ fontFamily: "inherit", width: "100%" }}
                />

                {/* Floating word count */}
                <motion.div
                  initial={{ opacity: 0, y: 10 }}
                  animate={{ opacity: 1, y: 0 }}
                  className="absolute bottom-4 right-4 bg-background/90 backdrop-blur-sm px-3 py-1 rounded-full text-sm text-muted-foreground border border-border/50"
                >
                  {wordCount.toLocaleString()} words • {readingTime} min read
                </motion.div>
              </div>

              {/* Keyboard shortcuts */}
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.5 }}
                className="flex items-center justify-center gap-6 text-xs text-muted-foreground bg-muted/30 rounded-lg p-4"
              >
                <span className="flex items-center gap-2">
                  <kbd className="px-2 py-1 bg-background rounded border">
                    ⌘S
                  </kbd>
                  Save
                </span>
              </motion.div>
            </div>
          ) : (
            <motion.div
              className={cn(
                "min-h-[600px] cursor-text transition-all duration-300 p-8 rounded-2xl",
                "hover:bg-gradient-to-br hover:from-muted/20 hover:to-transparent",
                "hover:shadow-lg hover:shadow-primary/5",
                "border border-transparent hover:border-border/50",
                // Maintain consistent width
                "max-w-none w-full"
              )}
              onClick={() => setIsEditing(true)}
              whileHover={{ scale: 1.005 }}
              whileTap={{ scale: 0.995 }}
            >
              {localSummary ? (
                <div
                  className="prose prose-xl max-w-none text-foreground w-full"
                  dangerouslySetInnerHTML={{
                    __html: `<p class='mb-6 leading-relaxed text-foreground/90 text-lg'>${renderMarkdown(localSummary)}</p>`,
                  }}
                />
              ) : (
                <motion.div
                  initial={{ opacity: 0 }}
                  animate={{ opacity: 1 }}
                  className="flex flex-col items-center justify-center text-center py-24"
                >
                  <motion.div
                    animate={{
                      rotate: [0, 5, -5, 0],
                      scale: [1, 1.1, 1],
                    }}
                    transition={{
                      duration: 2,
                      repeat: Number.POSITIVE_INFINITY,
                      repeatType: "reverse",
                    }}
                  >
                    <Edit3 className="w-16 h-16 text-primary/30 mb-6" />
                  </motion.div>
                  <h3 className="text-2xl font-semibold text-muted-foreground mb-2">
                    Start writing your chapter
                  </h3>
                  <p className="text-muted-foreground/70 text-lg">
                    Click anywhere to begin crafting your content
                  </p>
                </motion.div>
              )}
            </motion.div>
          )}
        </motion.div>
      </div>

      {/* Floating Action Button (when not editing) */}
      <AnimatePresence>
        {!isEditing && (
          <motion.div
            initial={{ opacity: 0, scale: 0, y: 100 }}
            animate={{ opacity: 1, scale: 1, y: 0 }}
            exit={{ opacity: 0, scale: 0, y: 100 }}
            className="fixed bottom-8 right-8 z-40"
          >
            <Button
              size="lg"
              onClick={() => setIsEditing(true)}
              className="rounded-full w-16 h-16 shadow-2xl shadow-primary/25 bg-primary hover:bg-primary/90 hover:scale-110 transition-all duration-300"
            >
              <Edit3 className="w-6 h-6" />
            </Button>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
