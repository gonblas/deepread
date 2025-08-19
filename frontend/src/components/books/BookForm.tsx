import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useForm } from "@/hooks/useForm";
import InputField from "@/components/form/InputField";

import { BOOK_GENRES, getGenreLabel } from "@/lib/genres";
import SelectField from "../form/SelectField";
import TextAreaField from "../form/TextAreaField";
import { Plus, User, X } from "lucide-react";
import { Input } from "../ui/input";
import FormFieldStructure from "../form/FormFieldStructure";

interface SendFunction {
  (data: {
    title: string;
    description: string;
    genre: string;
    authors: string[];
  }): Promise<boolean>;
}

interface BookFormProps {
  formTitle?: string;
  className?: string;
  sendFunction: SendFunction;
  initialValue?: {
    title: string;
    description: string;
    genre: string;
    authors: string[];
  };
}

export function BookForm({ formTitle, className, sendFunction, initialValue, ...props }: BookFormProps) {
  const { values, handleChange, errors, setErrors, resetForm } = useForm(initialValue || {
    title: "",
    description: "",
    genre: "",
    authors: [] as string[],
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const newErrors: { title?: string; genre?: string } = {};

    if (!values.title.trim()) {
      newErrors.title = "Title is required";
    }

    if (!values.genre) {
      newErrors.genre = "Genre is required";
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    setErrors({});

    const isCorrect = await sendFunction({
      title: values.title,
      description: values.description,
      genre: values.genre,
      authors: values.authors.filter((author) => author.trim() !== ""),
    });
    if (isCorrect) {
      resetForm();
    } else {
      setErrors({ title: "Failed to save book" });
    }
  };

  function updateAuthor(index: number, value: string): void {
    const newAuthors = [...values.authors];
    newAuthors[index] = value;
    handleChange({ target: { name: "authors", value: newAuthors } } as any);
  }

  function removeAuthor(index: number): void {
    const newAuthors = values.authors.filter((_, i) => i !== index);
    handleChange({ target: { name: "authors", value: newAuthors } } as any);
  }

  function addAuthor(): void {
    handleChange({
      target: { name: "authors", value: [...values.authors, ""] },
    } as any);
  }

  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Card>
        {formTitle &&
          <CardHeader className="text-center">
            <CardTitle className="text-xl sm:text-2xl">{formTitle}</CardTitle>
          </CardHeader>
        }
        <CardContent>
          <form className="grid gap-6" onSubmit={handleSubmit}>
            <div className="grid gap-8">
              <div className="grid gap-4 grid-rows-1 lg:grid-cols-[75%_23.5%] w-full">
                <InputField
                  label="Title"
                  id="title"
                  name="title"
                  type="text"
                  placeholder="Book title"
                  value={values.title}
                  onChange={handleChange}
                  error={errors.title}
                  required
                />
                <SelectField
                  label="Genre"
                  id="genre"
                  name="genre"
                  value={values.genre}
                  onValueChange={(value) =>
                    handleChange({ target: { name: "genre", value } } as any)
                  }
                  required
                  error={errors.genre}
                  options={BOOK_GENRES.map((genre) => ({
                    value: genre,
                    label: getGenreLabel(genre),
                  }))}
                />
              </div>

              <TextAreaField
                label="Description"
                id="description"
                name="description"
                placeholder="Book description"
                value={values.description}
                onChange={(e) => handleChange(e as any)}
                error={errors.description}
                className="min-h-20 h-36 max-h-52"
              />

              <FormFieldStructure
                label="Authors"
                id="authors"
                name="authors"
                error={errors.authors}
              >
                <div className="space-y-3">
                  {values.authors.map((author, index) => (
                    <div key={index} className="flex gap-2 items-center">
                      <div className="flex-1">
                        <div className="relative">
                          <User className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-gray-400" />
                          <Input
                            value={author}
                            onChange={(e) =>
                              updateAuthor(index, e.target.value)
                            }
                            placeholder={`Nombre del autor ${index + 1}`}
                            className="pl-10"
                          />
                        </div>
                      </div>
                      {values.authors.length > 1 && (
                        <Button
                          type="button"
                          variant="outline"
                          size="sm"
                          onClick={() => removeAuthor(index)}
                          className="h-9 text-red-500 hover:bg-red-50 hover:text-red-600"
                        >
                          <X className="size-5" />
                        </Button>
                      )}
                    </div>
                  ))}
                </div>

                <Button
                  type="button"
                  variant="outline"
                  size="sm"
                  onClick={addAuthor}
                  className="gap-2 bg-transparent mr-auto"
                >
                  <Plus className="h-4 w-4" />
                  Add Author
                </Button>
              </FormFieldStructure>

              <Button type="submit" className="w-full" onClick={handleSubmit}>
                Save Book
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
