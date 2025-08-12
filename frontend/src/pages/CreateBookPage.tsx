import AppLayout from "@/components/AppLayout";
import { BookForm } from "@/components/books/BookForm";
import { useAuth } from "@/contexts/authContext";
import { useNotification } from "@/contexts/notificationContext";
import Cookies from "js-cookie";

function CreateBookPage() {
  const { logout } = useAuth(); 
  const { showSuccess, showError } = useNotification();

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
        showSuccess("Book created successfully", `Book "${data.title}" has been created.`);
        return true;
      })
      .catch((error) => {
        showError("Failed to create book", error.message || "Unknown error");
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
