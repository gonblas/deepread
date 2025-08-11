import { Input } from "@/components/ui/input";
import FormFieldStructure from "./FormFieldStructure";

interface FormFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  id: string;
  error?: string;
  labelSuffix?: React.ReactNode;
}

export default function FormField({
  label,
  id,
  error,
  labelSuffix,
  ...props
}: FormFieldProps) {
  return (
    <FormFieldStructure label={label} id={id} error={error} labelSuffix={labelSuffix}>
      <Input id={id} {...props} />
    </FormFieldStructure>
  );
}