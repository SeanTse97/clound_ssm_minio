package edu.dgut.util;

import java.lang.reflect.Field;

public class CompareInfoUtil<T> {

    private T oldObj;
    private T newObj;

    public CompareInfoUtil(T o1, T o2){
        this.oldObj = o1;
        this.newObj = o2;
    }

    public T compare1to2(){
        Class c1 = oldObj.getClass();
        Class c2 = newObj.getClass();

        try {
            //获取所有属性
            Field[] fields = c2.getDeclaredFields();

            //遍历所有属性
            for (Field field : fields) {
                field.setAccessible(true);
                //遍历获取属性名
                String name = field.getName();

                // 在oldObject上调用get方法等同于获得oldObject的属性值
                Object oldValue = field.get(oldObj);
                // 在newObject上调用get方法等同于获得newObject的属性值
                Object newValue = field.get(newObj);

                if (newValue != null && oldValue !=null){
                    if(!newValue.equals(oldValue)  && newValue.toString().length() != 0 && newValue.toString() != "0" ){
                        if(newValue instanceof String){
                            field.set(oldObj,(String) newValue);
                        }else if(newValue instanceof Long){
                            if(!newValue.toString().equals("0")){
                                field.set(oldObj,(long) newValue);
                            }
                        }else if(newValue instanceof Integer){
                            field.set(oldObj,(int) newValue);
                        }
                        else{
                            field.set(oldObj,newValue);
                        }
                    }
                } else if (oldValue == null && newValue != null && newValue.toString().length() != 0 && newValue.toString() != "0"){

                    if(newValue instanceof String){
                        field.set(oldObj,(String) newValue);
                    }else if(newValue instanceof Long){
                        if(!newValue.toString().equals("0")){
                            field.set(oldObj,(long) newValue);
                        }
                    }else if(newValue instanceof Integer){
                        field.set(oldObj,(int) newValue);
                    }
                    else{
                        field.set(oldObj,newValue);
                    }
                }else{
                    continue;
                }

            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }finally {

            return oldObj;
        }
    }
}
