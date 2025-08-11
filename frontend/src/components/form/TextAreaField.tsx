import { Textarea } from "@/components/ui/textarea";
import FormFieldStructure from "./FormFieldStructure";

interface TextAreaFieldProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  label: string;
  id: string;
  error?: string;
  labelSuffix?: React.ReactNode;
}

export default function TextAreaField({
  label,
  id,
  error,
  labelSuffix,
  ...props
}: TextAreaFieldProps) {
  return (
    <FormFieldStructure label={label} id={id} error={error} labelSuffix={labelSuffix}>
      <Textarea id={id} {...props} />
    </FormFieldStructure>
  );
}
