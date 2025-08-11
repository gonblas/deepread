import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Label } from "@/components/ui/label"
import FormFieldStructure from "./FormFieldStructure"

interface SelectFieldProps {
  label: string
  id: string
  value: string
  onValueChange: (value: string) => void
  options: { value: string; label: string }[]
  error?: string
  required?: boolean
  labelSuffix?: React.ReactNode
  name?: string
  ariaDescribedById?: string
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
}: SelectFieldProps) {
  return (
    <FormFieldStructure label={label} id={id} error={error} labelSuffix={labelSuffix}>
      <Select
        id={id}
        name={name}
        value={value}
        onValueChange={onValueChange}
        aria-invalid={error ? "true" : "false"}
        aria-describedby={error ? ariaDescribedById || `${id}-error` : undefined}
        required={required}
      >
        <SelectTrigger className="w-full">
          <SelectValue placeholder={`Select a ${label.toLowerCase()}`} />
        </SelectTrigger>
        <SelectContent>
          {options.map(({ value: optValue, label: optLabel }) => (
            <SelectItem key={optValue} value={optValue}>
              {optLabel}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </FormFieldStructure>
  )
}
