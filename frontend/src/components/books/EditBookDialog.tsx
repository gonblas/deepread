import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { BookForm } from "./BookForm";
import { Edit } from "lucide-react";
import { Book } from "./BookCard";
import { useAuth } from "@/contexts/authContext";
import Cookies from "js-cookie";

interface EditBookDialogProps {
  book: Book;
}

export function EditBookDialog({ book }: EditBookDialogProps) {
  const { logout } = useAuth();
  const sendFunction = async (data: {
    title: string;
    description: string;
    genre: string;
    authors: string[];
  }) => {
    fetch(`http://localhost:8080/api/books/${book.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (!response.ok) {
          if (response.status === 401) {
            logout();
          }
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        localStorage.setItem("bookUpdated", data.title);
        window.location.reload();
        return true;
      })
      .catch((error) => {
        console.error("Error:", error);
        return false;
      });
    return true;
  };

  return (
    <Dialog>
      <form>
        <DialogTrigger asChild>
          <Button
            variant="outline"
            className="gap-2"
          >
            <Edit className="size-4 mr-1" />
            Edit
          </Button>
        </DialogTrigger>
        <DialogContent className="sm:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>Edit Book</DialogTitle>
            <DialogDescription>
              Make changes to your book here. Click save book when you&apos;re
              done.
            </DialogDescription>
          </DialogHeader>
          <BookForm sendFunction={sendFunction} initialValue={book} />
          <DialogFooter>
            <DialogClose asChild>
              <Button variant="outline">Cancel</Button>
            </DialogClose>
          </DialogFooter>
        </DialogContent>
      </form>
    </Dialog>
  );
}
