import { BookOpen, Brain, Home, Settings, Plus, FileText } from "lucide-react";
import type { SidebarItemsData } from "@/contexts/sidebarContext";

export const defaultSidebar: SidebarItemsData = [
  {
    title: "Main",
    items: [
      { title: "Dashboard", url: "/", icon: Home },
      { title: "My Books", url: "/books", icon: BookOpen },
      { title: "My Quizzes", url: "/my-quizzes", icon: Brain },
    ],
  },
  {
    title: "Management",
    items: [
      { title: "Create Quiz", url: "/create", icon: Plus },
      { title: "Settings", url: "/settings", icon: Settings },
    ],
  },
];

export const booksSidebar: SidebarItemsData = [
  {
    title: "Main",
    items: [
      { title: "Dashboard", url: "/", icon: Home },
      { title: "My Quizzes", url: "/my-quizzes", icon: Brain },
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
  chapters: { id: string; number: number; title: string }[]
): SidebarItemsData => [
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
      {title: "Add Chapter", url: `/books/${bookId}/chapters/create`, icon: Plus },
      {title: "All Chapters", url: `/books/${bookId}/chapters`, collapsible: true, icon: FileText, items: [
        ...chapters.map((chapter) => ({
          title: `${chapter.number}. ${chapter.title}`,
          url: `/books/${bookId}/chapters/${chapter.id}`,
        })),
      ]},
    ],
  },
  {
    title: "Quizzes",
    items: [
      { title: "All Quizzes", url: `/books/${bookId}/quizzes`, icon: Brain },
      { title: "Create Quiz", url: `/books/${bookId}/quizzes/create`, icon: Plus },
    ],
  },
];
