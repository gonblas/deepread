import AppLayout from "@/components/AppLayout";
import { BooksSearch } from "@/components/books/BookSearch";

function BookPage() {
  return (
    <AppLayout>
      <div className="flex flex-1 flex-col gap-4 p-4 pt-0">
        <BooksSearch />
      </div>
    </AppLayout>
  );
}

export default BookPage;
