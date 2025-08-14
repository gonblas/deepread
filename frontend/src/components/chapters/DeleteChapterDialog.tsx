"use client";

import { useAuth } from "@/contexts/authContext";
import { DeleteElementDialog } from "../DeleteElementDialog";
import { useDataRefresh } from "@/contexts/dataRefreshContext";
import Cookies from "js-cookie";

interface DeleteChapterDialogProps {
  chapterTitle: string;
  bookId: string;
  chapterId: string;
}

export function DeleteChapterDialog({ chapterTitle, bookId, chapterId }: DeleteChapterDialogProps) {
  const { logout } = useAuth();
  const { deleteResource } = useDataRefresh();

  const handleDelete = async () => {
    const responseFunc = async () => {
      const response = await fetch(`http://localhost:8080/api/books/${bookId}/chapters/${chapterId}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${Cookies.get("token")}`,
        },
      });

      if (!response.ok) {
        if (response.status === 401) {
          logout();
          throw new Error("Authentication failed");
        } else {
          throw new Error(
            `Failed to delete book: ${response.statusText}`
          );
        }
      }

      if (response.status === 204 || response.ok) {
        return;
      }

      const text = await response.text();
      return text ? JSON.parse(text) : null;
    };

    try {
      await deleteResource(
        "chapter",
        responseFunc,
        `Chapter "${chapterTitle}" has been deleted successfully`,
        "/books/" + bookId
      );
    } catch (error) {
      console.error("Delete operation failed:", error);
    }
  };

  return (
    <DeleteElementDialog onDelete={handleDelete} elementName={chapterTitle} resourceName="Chapter" fullWidth={true} />
  );
}
