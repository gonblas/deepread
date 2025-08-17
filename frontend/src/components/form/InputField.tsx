import { Input } from "@/components/ui/input";
import FormFieldStructure from "./FormFieldStructure";
import React from "react";

interface FormFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
  icon?: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  labelSuffix?: React.ReactNode;
}

export default function InputField({
  label,
  id,
  error,
  labelSuffix,
  icon,
  ...props
}: FormFieldProps) {
  return (
    <FormFieldStructure
      label={label}
      id={id}
      error={error}
      labelSuffix={labelSuffix}
    >
      {icon &&
        React.createElement(icon, {
          className:
            "absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4",
        })}
      <Input id={id} {...props} className={icon ? "pl-10" : ""} />
    </FormFieldStructure>
  );
}
