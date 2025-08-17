import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import FormFieldStructure from "./FormFieldStructure";
import React from "react";

interface SelectFieldProps {
  label: string;
  id: string;
  value: string;
  onValueChange: (value: string) => void;
  options: { value: string; label: string }[];
  error?: string;
  required?: boolean;
  labelSuffix?: React.ReactNode;
  name?: string;
  ariaDescribedById?: string;
  icon?: React.ComponentType<React.SVGProps<SVGSVGElement>>;
}

export default function SelectField({
  label,
  id,
  value,
  onValueChange,
  options,
  error,
  required,
  labelSuffix,
  name,
  ariaDescribedById,
  icon,
}: SelectFieldProps) {
  const hasIcon = Boolean(icon);

  return (
    <FormFieldStructure
      label={label}
      id={id}
      error={error}
      labelSuffix={labelSuffix}
    >
      <Select
        name={name}
        value={value}
        onValueChange={onValueChange}
        aria-invalid={error ? "true" : "false"}
        aria-describedby={
          error ? ariaDescribedById || `${id}-error` : undefined
        }
        required={required}
      >
        <SelectTrigger className={hasIcon ? "w-full pl-10" : "w-full"}>
          {hasIcon && (
            <span className="absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none flex items-center">
              {React.createElement(icon, {
                className: "text-muted-foreground h-4 w-4",
              })}
            </span>
          )}
          <SelectValue placeholder={`Select a ${label.toLowerCase()}`} />
        </SelectTrigger>
        <SelectContent>
          {options.map(({ value: optValue, label: optLabel }) => (
            <SelectItem
              key={optValue}
              value={optValue}
            >
              {optLabel}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </FormFieldStructure>
  );
}
