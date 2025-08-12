"use client";

import { useAuth } from "@/contexts/authContext";
import { DeleteElementDialog } from "../DeleteElementDialog";
import Cookies from "js-cookie";
import { useDataRefresh } from "@/contexts/dataRefreshContext";

interface DeleteBookDialogProps {
  bookTitle: string;
  bookId: string;
}

export function DeleteBookDialog({ bookTitle, bookId }: DeleteBookDialogProps) {
  const { logout } = useAuth();
  const { deleteResource } = useDataRefresh();

  const handleDelete = async () => {
    const responseFunc = async () => {
      const response = await fetch(`http://localhost:8080/api/book/${bookId}`, {
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
            `Failed to delete book: ${response.status} ${response.statusText}`
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
        "book",
        responseFunc,
        `Book "${bookTitle}" has been deleted successfully`,
        "/books"
      );
    } catch (error) {
      console.error("Delete operation failed:", error);
    }
  };

  return (
    <DeleteElementDialog onDelete={handleDelete} elementName={bookTitle} />
  );
}
