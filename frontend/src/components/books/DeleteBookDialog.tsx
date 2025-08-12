import { useAuth } from "@/contexts/authContext";
import { DeleteElementDialog } from "../DeleteElementDialog";
import Cookies from "js-cookie";

interface DeleteBookDialogProps {
  bookTitle: string;
  bookId: string;
}

export function DeleteBookDialog({ bookTitle, bookId }: DeleteBookDialogProps) {
  const { logout } = useAuth();
  const handleDelete = async () => {
    fetch(`http://localhost:8080/api/book/${bookId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
    }).then((response) => {
      if (!response.ok) {
        if (response.status === 401) {
          logout();
          return;
        } else {
          throw new Error("Failed to delete book");
        }
      }
      if (response.status === 204) {
        localStorage.setItem("bookDeleted", bookTitle);
        window.location.reload();
        return;
      }
      return response.text().then((text) => (text ? JSON.parse(text) : null));
    });
  };

  return (
    <DeleteElementDialog onDelete={handleDelete} elementName={bookTitle} />
  );
}
