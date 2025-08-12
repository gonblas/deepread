import AppLayout from "@/components/AppLayout";
import { BookForm } from "@/components/books/BookForm";
import { useAuth } from "@/contexts/authContext";
import Cookies from "js-cookie";
import { toast } from "sonner";

function CreateBookPage() {
  const { logout } = useAuth(); 

  const sendFunction = async (data: {
    title: string;
    description: string;
    genre: string;
    authors: string[];
  }) => {
    fetch("http://localhost:8080/api/book", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${Cookies.get("token")}`,
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (!response.ok) {
          if(response.status === 401) {
            logout();
          }
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((data) => {
        toast.success("Book created successfully", {
          description: `Book "${data.title}" has been created.`,
        });
        return true;
      })
      .catch((error) => {
        console.error("Error:", error);
        return false;
      });
    return true;
  }

  return (
    <AppLayout>
      <BookForm formTitle="Create a new book" sendFunction={sendFunction} />
    </AppLayout>
  );
}

export default CreateBookPage;
