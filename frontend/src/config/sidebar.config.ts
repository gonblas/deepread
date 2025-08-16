import { BookOpen, Brain, Home, Settings, Plus, FileText } from "lucide-react";
import type { SidebarItemsData } from "@/contexts/sidebarContext";

export const defaultSidebar: SidebarItemsData = [
  {
    title: "Main",
    items: [
      { title: "Dashboard", url: "/", icon: Home },
      { title: "My Books", url: "/books", icon: BookOpen },
      { title: "My Quizzes", url: "/quizzes", icon: Brain },
    ],
  },
  {
    title: "Management",
    items: [
      { title: "Settings", url: "/settings", icon: Settings },
    ],
  },
];

export const booksSidebar: SidebarItemsData = [
  {
    title: "Main",
    items: [
      { title: "Dashboard", url: "/", icon: Home },
      { title: "My Quizzes", url: "/quizzes", icon: Brain },
    ],
  },
  {
    title: "Books",
    items: [
      { title: "My Books", url: "/books", icon: BookOpen },
      { title: "Add Book", url: "/books/create", icon: Plus },
    ],
  },
];

export const selectedBookSidebar = (
  bookId: string,
  chapters: { id: string; number: number; title: string }[],
  chapterId?: string
): SidebarItemsData => [
  {
    title: "Main",
    items: [
      { title: "Dashboard", url: "/", icon: Home },
      { title: "My Quizzes", url: "/quizzes", icon: Brain },
    ],
  },
  {
    title: "Books",
    items: [
      { title: "My Books", url: "/books", icon: BookOpen },
      { title: "Add Book", url: "/books/create", icon: Plus },
    ],
  },
  {
    title: "Chapters",
    items: [
      { title: "Add Chapter", url: `/books/${bookId}/chapters/create`, icon: Plus },
      ...(chapters.length > 0
        ? [{
            title: "All Chapters",
            url: `/books/${bookId}/chapters`,
            collapsible: true,
            icon: FileText,
            items: chapters.map((chapter) => ({
              title: `${chapter.number}. ${chapter.title}`,
              url: `/books/${bookId}/chapters/${chapter.id}`,
              active: (chapterId === chapter.id ? true : undefined),
            })),
          }]
        : []
      ),
    ],
  },
];
