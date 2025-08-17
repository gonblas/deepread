import { Button } from "@/components/ui/button";
import { Calendar } from "lucide-react";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Calendar as CalendarComponent } from "@/components/ui/calendar";
import { cn } from "@/lib/utils";
import { format } from "date-fns";
import FormFieldStructure from "./FormFieldStructure";

interface DateFieldProps {
  label: string;
  id: string;
  value?: Date;
  onValueChange: (date?: Date) => void;
  error?: string;
  required?: boolean;
  name?: string;
  minDate?: Date;
  maxDate?: Date;
  disabled?: boolean;
}

export default function DateField({
  label,
  id,
  value,
  onValueChange,
  error,
  required,
  name,
  minDate,
  maxDate,
  disabled,
}: DateFieldProps) {
  return (
    <FormFieldStructure label={label} id={id} error={error}>
      <Popover>
        <PopoverTrigger asChild>
          <Button
            variant="outline"
            name={name}
            className={cn(
              "w-full justify-start text-left font-normal",
              !value && "text-muted-foreground"
            )}
            disabled={disabled}
            aria-invalid={!!error}
            aria-describedby={error ? `${id}-error` : undefined}
            required={required}
          >
            <Calendar className="mr-2 h-4 w-4" />
            {value ? format(value, "PPP") : `Pick a date`}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="start">
          <CalendarComponent
            mode="single"
            selected={value}
            onSelect={onValueChange}
            disabled={(date) => {
              if (maxDate && date > maxDate) return true;
              if (minDate && date < minDate) return true;
              return false;
            }}
          />
        </PopoverContent>
      </Popover>
    </FormFieldStructure>
  );
}
